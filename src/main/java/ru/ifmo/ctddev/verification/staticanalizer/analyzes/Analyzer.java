package ru.ifmo.ctddev.verification.staticanalizer.analyzes;

import com.github.javaparser.Position;
import com.github.javaparser.ast.stmt.Statement;

import javax.annotation.Nonnull;

public interface Analyzer {
    @Nonnull
    String analyze(@Nonnull Statement statement);

    @Nonnull
    String getErrorName();

    default String positionToString(@Nonnull Position position) {
        if (position.line == -1 || position.column == -1) {
            return "Unknown position";
        }
        return "Line: " + position.line + ", column: " + position.column;
    }
}
