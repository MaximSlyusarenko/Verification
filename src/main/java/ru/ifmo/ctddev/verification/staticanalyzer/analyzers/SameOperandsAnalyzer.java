package ru.ifmo.ctddev.verification.staticanalyzer.analyzers;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;

public class SameOperandsAnalyzer implements Analyzer {

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
        return "Same operands";
    }

    class MyVisitor extends VoidVisitorAdapter<StringBuilder> {

        @Override
        public void visit(BinaryExpr binaryExpr, StringBuilder out) {
            BinaryExpr.Operator op = binaryExpr.getOperator();
            if (! (op.equals(BinaryExpr.Operator.PLUS)
                    || op.equals(BinaryExpr.Operator.MULTIPLY)
                    || op.equals(BinaryExpr.Operator.LEFT_SHIFT)
                    || op.equals(BinaryExpr.Operator.SIGNED_RIGHT_SHIFT)
                    || op.equals(BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT))
            ) {
                if (binaryExpr.getLeft().equals(binaryExpr.getRight())) {
                    printPosition(out, binaryExpr);
                }
            }
            super.visit(binaryExpr, out);
        }

        @Override
        public void visit(AssignExpr assignExpr, StringBuilder out) {
            AssignExpr.Operator operator = assignExpr.getOperator();
            if (assignExpr.getTarget().equals(assignExpr.getValue())
                    && !operator.equals(AssignExpr.Operator.MULTIPLY)
                    && !operator.equals(AssignExpr.Operator.PLUS)
            ) {
                printPosition(out, assignExpr);
            }
            super.visit(assignExpr, out);
        }
    }
}
