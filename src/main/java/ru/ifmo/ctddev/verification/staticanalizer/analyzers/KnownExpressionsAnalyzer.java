package ru.ifmo.ctddev.verification.staticanalizer.analyzers;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.*;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KnownExpressionsAnalyzer implements Analyzer {
    @Nonnull
    private final Set<Expression> alwaysTrueExpressions;

    public KnownExpressionsAnalyzer() {
        this.alwaysTrueExpressions = new HashSet<>();
    }

    @Nonnull
    @Override
    public String analyze(@Nonnull Statement statement) {
        StringBuilder result = new StringBuilder();
        Set<Expression> notContainedKnownExpressions = new HashSet<>();
        if (statement instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) statement;
            Expression ifCondition = ifStmt.getCondition();
            Set<Expression> knownExpressions = findKnownExpressions(ifCondition);
            for (Expression knownExpression : knownExpressions) {
                if (!alwaysTrueExpressions.contains(knownExpression)) {
                    notContainedKnownExpressions.add(knownExpression);
                }
            }
            Set<ExpressionWithValue> allKnownExpressions = findAllKnownExpressions(ifCondition);
            if (!allKnownExpressions.isEmpty()) {
                result.append("For if statement ");
                printPosition(result, statement);
                result.append("known expressions: \n");
                for (ExpressionWithValue knownExpression : allKnownExpressions) {
                    result.append(knownExpression.toString()).append("\n");
                }
            }
            alwaysTrueExpressions.addAll(notContainedKnownExpressions);
        }
        for (Node node : statement.getChildNodes()) {
            if (node instanceof Statement) {
                Statement childStatement = (Statement) node;
                result.append(analyze(childStatement));
            }
        }
        alwaysTrueExpressions.removeAll(notContainedKnownExpressions);
        return result.toString();
    }

    private Set<Expression> findKnownExpressions(Expression expression) {
        Set<Expression> result = new HashSet<>();
        if (expression instanceof EnclosedExpr) {
            result.addAll(findKnownExpressions(((EnclosedExpr) expression).getInner()));
        } else if (expression instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr) expression;
            result.add(unaryExpr);
        } else if (expression instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr) expression;
            if (binaryExpr.getOperator() == BinaryExpr.Operator.AND) {
                result.add(binaryExpr.getLeft());
                result.add(binaryExpr.getRight());
                result.addAll(findKnownExpressions(binaryExpr.getLeft()));
                result.addAll(findKnownExpressions(binaryExpr.getRight()));
            }
        }
        return result;
    }

    @Nonnull
    private Set<ExpressionWithValue> findAllKnownExpressions(@Nonnull Expression expression) {
        Set<ExpressionWithValue> result = new HashSet<>();
        // TODO: logic
        return result;
    }

    @Nonnull
    @Override
    public String getErrorName() {
        return "Always true or always false expressions analyzer";
    }

    private class ExpressionWithValue {
        @Nonnull
        private final Expression expression;
        private final boolean value;

        private ExpressionWithValue(Expression expression, boolean value) {
            this.expression = expression;
            this.value = value;
        }

        @Override
        public String toString() {
            return expression + " has value " + value;
        }
    }
}
