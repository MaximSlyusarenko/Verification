package ru.ifmo.ctddev.verification.staticanalizer;

public class ToFindError {

    public void something() {
        try {
            System.out.println("123");
        } catch (IllegalArgumentException e) {
            System.out.println("234");
        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {

        } catch (AssertionError ignored) {

        }
    }
}
