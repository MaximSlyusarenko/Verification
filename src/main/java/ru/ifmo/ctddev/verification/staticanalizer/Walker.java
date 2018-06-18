package ru.ifmo.ctddev.verification.staticanalizer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class Walker {

    private final String errorFileName;
    private final List<Analyzer> analyzers;

    public Walker(@Nonnull String errorsFileName, @Nonnull List<Analyzer> analyzers) {
        this.errorFileName = requireNonNull(errorsFileName, "errorFileName");
        this.analyzers = Collections.unmodifiableList(requireNonNull(analyzers, "analyzers"));
    }

    public void runAndAnalyze(@Nonnull String rootPath) throws IOException {
        Path errorFilePath = Paths.get(errorFileName);
        if (errorFilePath.getParent() != null) {
            Files.createDirectories(errorFilePath.getParent());
        }
        Files.write(errorFilePath, ("Analyzing started at " + System.currentTimeMillis() + "\n").getBytes());
        Files.walk(Paths.get(rootPath))
                .filter(path -> !Files.isDirectory(path))
                .map(Path::toFile)
                .filter(file -> file.getName().endsWith(".java"))
                .forEach(file -> {
                    try {
                        analyzeFile(file);
                    } catch (IOException ignored) {}
                });
    }

    private void analyzeFile(File fileToAnalyze) throws IOException {
        Path errorFilePath = Paths.get(errorFileName);
        CompilationUnit compilationUnit;
        try {
            compilationUnit = JavaParser.parse(fileToAnalyze);
        } catch (Exception ignored) {
            return;
        }
        for (Analyzer analyzer : analyzers) {
            for (TypeDeclaration<?> classDeclaration : compilationUnit.getTypes()) {
                if (!(classDeclaration instanceof ClassOrInterfaceDeclaration)) {
                    continue;
                }
                StringBuilder classResult = new StringBuilder();
                for (MethodDeclaration methodDeclaration : classDeclaration.getMethods()) {
                    methodDeclaration.getBody().ifPresent(methodBody -> {
                        String resultOfMethod = analyzer.analyze(methodBody);
                        if (!resultOfMethod.isEmpty()) {
                            classResult.append("In method ").append(methodDeclaration.getNameAsString()).append(" ")
                                    .append(analyzer.getErrorName()).append(":\n").append(resultOfMethod).append("\n");
                        }
                    });
                }
                if (!(classResult.length() == 0)) {
                    String toPrint = "WARNING: \n" + classDeclaration.getNameAsString() + ":\n" + classResult;
                    Files.write(errorFilePath, toPrint.getBytes(), StandardOpenOption.APPEND);
                }
            }
        }
    }
}
