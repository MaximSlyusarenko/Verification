package ru.ifmo.ctddev.verification.staticanalizer;

import com.github.javaparser.ast.stmt.Statement;

import javax.annotation.Nonnull;

public interface Analyzer {
    @Nonnull
    String analyze(@Nonnull Statement compilationUnit);

    @Nonnull
    String getErrorName();
}
