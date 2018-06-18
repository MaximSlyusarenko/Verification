package ru.ifmo.ctddev.verification.staticanalizer;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.*;

import javax.annotation.Nonnull;
import java.util.List;

public class EmptyExceptionHandlerAnalyzer implements Analyzer {

    private static final String IGNORED_VARIABLE_IN_EXCEPTION_HANDLER_NAME = "ignored";

    @Nonnull
    @Override
    public String analyze(@Nonnull Statement statement) {
        StringBuilder result = new StringBuilder();
        if (statement instanceof TryStmt) {
            TryStmt tryStmt = (TryStmt) statement;
            NodeList<CatchClause> catchClauses = tryStmt.getCatchClauses();
            for (CatchClause catchClause : catchClauses) {
                if (IGNORED_VARIABLE_IN_EXCEPTION_HANDLER_NAME.equals(catchClause.getParameter().getNameAsString())) {
                    continue;
                }
                List<Node> childNodes = catchClause.getChildNodes();
                boolean isEmpty = childNodes.isEmpty();
                if (!isEmpty) {
                    isEmpty = true;
                    for (Node childNode : childNodes) {
                        if (childNode instanceof Parameter) {
                            // skip Parameter statement because it is just a variable declaration
                            continue;
                        }
                        if (childNode instanceof BlockStmt) {
                            isEmpty &= isBlockStatementEmpty((BlockStmt) childNode);
                        }
                    }
                }
                if (isEmpty) {
                    result.append("Begin: ")
                            .append(positionToString(catchClause.getBegin().orElse(new Position(-1, -1))))
                            .append(", End:")
                            .append(positionToString(catchClause.getEnd().orElse(new Position(-1, -1))))
                            .append("\n");
                }
            }
        }
        for (Node node : statement.getChildNodes()) {
            if (node instanceof Statement) {
                Statement childStatement = (Statement) node;
                result.append(analyze(childStatement));
            }
        }
        return result.toString();
    }

    @Nonnull
    @Override
    public String getErrorName() {
        return "Empty exception handler";
    }

    private boolean isBlockStatementEmpty(BlockStmt blockStmt) {
        List<Node> childNodes = blockStmt.getChildNodes();
        if (childNodes.isEmpty()) {
            return true;
        }
        boolean isEmpty = true;
        for (Node node : childNodes) {
            isEmpty &= node instanceof EmptyStmt;
        }
        return isEmpty;
    }
}
