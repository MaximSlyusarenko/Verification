package ru.ifmo.ctddev.verification.staticanalizer;

import com.github.javaparser.ast.stmt.Statement;

import javax.annotation.Nonnull;

public class EmptyExceptionHandlerAnalyzer implements Analyzer {

    @Nonnull
    @Override
    public String analyze(@Nonnull Statement compilationUnit) {
        // TODO:
        return "";
    }

    @Nonnull
    @Override
    public String getErrorName() {
        return "Empty exception handler";
    }
}
