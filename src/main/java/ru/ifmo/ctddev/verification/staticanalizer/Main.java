package ru.ifmo.ctddev.verification.staticanalizer;

import ru.ifmo.ctddev.verification.staticanalizer.analyzers.BitwiseOperatorsAnalyzer;
import ru.ifmo.ctddev.verification.staticanalizer.analyzers.EmptyExceptionHandlerAnalyzer;
import ru.ifmo.ctddev.verification.staticanalizer.analyzers.KnownExpressionsAnalyzer;
import ru.ifmo.ctddev.verification.staticanalizer.analyzers.SameOperandsAnalyzer;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if (args == null || args.length != 1 || args[0] == null) {
            System.out.println("Usage: java -jar analizer.jar [project-root]");
            return;
        }
        try (Writer out = createWriter()) {
            Walker walker = new Walker(out,
                    Arrays.asList(
                            new EmptyExceptionHandlerAnalyzer(),
                            new BitwiseOperatorsAnalyzer(),
                            new SameOperandsAnalyzer(),
                            new KnownExpressionsAnalyzer()
                    )
            );
            walker.runAndAnalyze(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Writer createWriter() throws IOException {
        String outFile = "analyzer-output.out";
        Path outPath = Paths.get(outFile);
        if (outPath.getParent() != null) {
            Files.createDirectories(outPath.getParent());
        }
        return Files.newBufferedWriter(outPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
