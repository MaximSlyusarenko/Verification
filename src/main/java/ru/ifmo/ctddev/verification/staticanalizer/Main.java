package ru.ifmo.ctddev.verification.staticanalizer;

import java.io.IOException;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        if (args == null || args.length != 1 || args[0] == null) {
            System.out.println("Usage: java -jar analizer.jar [project-root]");
            return;
        }
        Walker walker = new Walker("analyzer-output.out", Collections.emptyList());
        try {
            walker.runAndAnalyze(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
