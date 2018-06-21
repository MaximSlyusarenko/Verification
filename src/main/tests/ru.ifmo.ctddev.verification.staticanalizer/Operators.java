package ru.ifmo.ctddev.verification.staticanalizer;

public class Operators {

    void bitwise() {
        int a = 1;
        int b = 2;
        int zer = 0;

        if ((a | 0) == 1) {
            System.out.println("not ok");
        }

        if ((a & 0) == 0) {
            System.out.println("not ok");
        }

        int c = a << 0;
        int ok = a << 1;
        int d = a >> 0;

    }

    void sameValue() {
        int a = 0;
        a = a;
        if (1 == 1) {
            a = 3 + (a - a);
        }

        int ok = 1 + 1;
        int ok2 = 2 * 2;
        ok += ok;
        ok2 *= ok2;
        if (a + 1 == a + 1) {
            System.out.println("not ok");
        }
    }
}
