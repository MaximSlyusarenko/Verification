package ru.ifmo.ctddev.verification.staticanalizer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import ru.ifmo.ctddev.verification.staticanalizer.analyzes.Analyzer;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class Walker {

    private final Writer out;
    private final List<Analyzer> analyzers;

    public Walker(@Nonnull Writer outStream, @Nonnull List<Analyzer> analyzers) {
        this.out = requireNonNull(outStream, "outStream");
        this.analyzers = Collections.unmodifiableList(requireNonNull(analyzers, "analyzers"));
    }

    public void runAndAnalyze(@Nonnull String rootPath) throws IOException {
        out.write("Analyzing started at " + System.currentTimeMillis() + "\n");
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
                            classResult.append("Method \"").append(methodDeclaration.getNameAsString()).append("\": ")
                                    .append(analyzer.getErrorName()).append(":\n")
                                    .append(resultOfMethod).append("\n");
                        }
                    });
                }
                if (!(classResult.length() == 0)) {
                    out.write(
                            "WARNING: \n" +
                                    getFullyQualifiedName(compilationUnit, classDeclaration) + ":\n" +
                                    classResult
                    );
                }
            }
        }
    }

    private String getFullyQualifiedName(CompilationUnit compilationUnit, TypeDeclaration<?> classDeclaration) {
        return compilationUnit.getPackageDeclaration()
                .map(PackageDeclaration::getName)
                .map(Node::toString)
                .map(s -> s + ".")
                .orElse("")
                + classDeclaration.getNameAsString();
    }
}
