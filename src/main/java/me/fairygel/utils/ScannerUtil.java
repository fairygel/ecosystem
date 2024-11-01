package me.fairygel.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ScannerUtil {
    private final Scanner scanner;

    public ScannerUtil() {
        scanner = new Scanner(System.in);
    }

    public int nextInt() {
        try {
            int input = scanner.nextInt();
            nextLine();

            return input;
        } catch (InputMismatchException ignored) {
            System.out.println("Invalid input, please enter a number.");
            nextLine();

            return nextInt();
        }
    }

    public long nextLong() {
        try {
            long input = scanner.nextLong();
            nextLine();

            return input;
        } catch (InputMismatchException ignored) {
            System.out.println("Invalid input, please enter a number.");
            nextLine();

            return nextLong();
        }
    }

    public String nextLine() {
        return scanner.nextLine();
    }
}
