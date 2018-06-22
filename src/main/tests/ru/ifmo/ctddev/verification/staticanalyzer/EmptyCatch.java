package ru.ifmo.ctddev.verification.staticanalyzer;

public class EmptyCatch {

    public void bad() {
        try {
            System.out.println("123");
        } catch (IllegalArgumentException e) {
            System.out.println("234");
        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {

        } catch (AssertionError ignored) {

        }
    }

    private void good() {
        try {
            System.out.println("123");
        } catch (IllegalArgumentException e) {
            System.out.println("234");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("234");
        } catch (Exception e) {
            System.out.println("234");
        } catch (AssertionError ignored) {
            System.out.println("234");
        }
    }
}
