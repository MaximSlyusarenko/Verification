package ru.ifmo.ctddev.verification.staticanalizer;

public class UselessBitwise {

    void test() {
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
}
