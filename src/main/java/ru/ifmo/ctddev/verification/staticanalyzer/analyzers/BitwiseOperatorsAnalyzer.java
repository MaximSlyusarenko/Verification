package ru.ifmo.ctddev.verification.staticanalyzer.analyzers;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;

public class BitwiseOperatorsAnalyzer implements Analyzer {
    @Nonnull
    @Override
    public String analyze(@Nonnull Statement statement) {
        StringBuilder sb = new StringBuilder();
        statement.accept(new MyVisitor(), sb);
        return sb.toString();
    }

    @Nonnull
    @Override
    public String getErrorName() {
        return "Useless bitwise operation";
    }

    @Override
    public void refreshAnalyzerForNewMethod() {}

    class MyVisitor extends VoidVisitorAdapter<StringBuilder> {
        @Override
        public void visit(BinaryExpr binaryExpr, StringBuilder out) {
            BinaryExpr.Operator op = binaryExpr.getOperator();
            if (op.equals(BinaryExpr.Operator.BINARY_AND)
                    || op.equals(BinaryExpr.Operator.BINARY_OR)
                    || op.equals(BinaryExpr.Operator.LEFT_SHIFT)
                    || op.equals(BinaryExpr.Operator.SIGNED_RIGHT_SHIFT)
                    || op.equals(BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT)
            ) {
                if (isZero(binaryExpr.getLeft()) || isZero(binaryExpr.getRight())) {
                   printPosition(out, binaryExpr);
                }
            }
            super.visit(binaryExpr, out);
        }

        private boolean isZero(Expression expr) {
            if (expr instanceof IntegerLiteralExpr && ((IntegerLiteralExpr) expr).getValue().equals("0")) {
                return true;
            }
            return false;
        }
    }
}
