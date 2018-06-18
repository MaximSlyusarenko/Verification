package ru.ifmo.ctddev.verification.staticanalizer;

import com.github.javaparser.ast.CompilationUnit;

public interface Analyzer {
    String analyze(CompilationUnit compilationUnit, String fileName);
}
