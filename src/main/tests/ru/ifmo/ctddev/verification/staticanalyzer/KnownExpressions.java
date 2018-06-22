package ru.ifmo.ctddev.verification.staticanalyzer;

public class KnownExpressions {

    public void testSimpleKnownExpressions() {
        Boolean a = null;
        Boolean b = null;
        if (a && b) {
            if (a) {
                System.out.println("This if is always true, 1");
            }
            if (!a) {
                System.out.println("This if is always false, 2");
            }
            boolean c = false;
            if (a || c) {
                System.out.println("This if is always true, 3");
            }
        }
    }

    public void testMoreComplexKnownExpressions() {
        Boolean a = null;
        Boolean b = null;
        Boolean c = null;
        Boolean d = null;
        Boolean e = null;
        if ((a || b) && (c && d) && !e) {
            if (a) {
                System.out.println("This is normal expression");
            }
            if (c) {
                System.out.println("This if is always true, 1");
            }
            if (d) {
                System.out.println("This if is always true, 2");
            }
            if (c && !e) {
                System.out.println("This if is always true, 3");
            }
            if (c || e) {
                System.out.println("This if is always true, 4");
            }
            if ((c && !e) && !d) {
                System.out.println("This if is always false, 5");
            }
            if ((!c && !e) || !d) {
                System.out.println("This if is always false, 6");
            }
            if ((!c && e) || d) {
                System.out.println("This if is always true, 7");
            }
        }
    }
}
