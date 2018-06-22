package ru.ifmo.ctddev.verification.staticanalyzer.analyzers;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithRange;
import com.github.javaparser.ast.stmt.Statement;

import javax.annotation.Nonnull;

public interface Analyzer {
    @Nonnull
    String analyze(@Nonnull Statement statement);

    @Nonnull
    String getErrorName();

    void refreshAnalyzerForNewMethod();

    default String positionToString(@Nonnull Position position) {
        if (position.line == -1 || position.column == -1) {
            return "Unknown position";
        }
        return "Line: " + position.line + ", column: " + position.column;
    }

    default void printPosition(StringBuilder result, NodeWithRange<Node> node) {
        result.append("Begin: ")
                .append(positionToString(node.getBegin().orElse(new Position(-1, -1))))
                .append(", End:")
                .append(positionToString(node.getEnd().orElse(new Position(-1, -1))))
                .append("\n\"")
                .append(node.toString())
                .append("\"\n");
    }

}
