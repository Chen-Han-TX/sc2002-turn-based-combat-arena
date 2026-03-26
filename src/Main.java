/**
 * Entry point for the Turn-Based Combat Arena game.
 * Owner: Person E
 */
import ui.GameUI;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameUI ui = new GameUI();
        ui.showLoadingScreen();

        // Keep default execution non-blocking for smoke tests.
        if (args.length == 0 || !"--interactive".equals(args[0])) {
            System.out.println("\nUI ready. Waiting for engine integration.");
            System.out.println("Run with --interactive to test setup prompts.");
            return;
        }

        int playerChoice = ui.promptPlayerChoice();
        List<Integer> itemChoices = ui.promptItemChoices(
            List.of("Potion", "Power Stone", "Smoke Bomb"), 2
        );
        int difficultyChoice = ui.promptDifficultyChoice();

        System.out.println("\nSetup complete:");
        System.out.println("Player index: " + playerChoice);
        System.out.println("Item indices: " + itemChoices);
        System.out.println("Difficulty index: " + difficultyChoice);
        System.out.println("Waiting for engine integration.");
    }
}
