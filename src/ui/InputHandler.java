package ui;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prompt until a valid integer is enter
     */
    public int promptIndex(String label, int min, int max) {
        while (true) {
            System.out.print(label + " (" + min + "-" + max + "): ");
            String raw = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(raw);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Keep looping with feedback
            }

            System.out.println("Invalid input. Enter a number from " + min + " to " + max + ".");
        }
    }
}
