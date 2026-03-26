package ui;

import model.combatant.Combatant;
import java.util.List;
import java.util.Scanner;

/**
 * Owner: Person E
 * Handles all CLI display and user input.
 * Separated from battle logic (UI should not contain game rules).
 */
public class GameUI {
    private final InputHandler inputHandler;

    public GameUI() {
        this.inputHandler = new InputHandler(new Scanner(System.in));
    }

    /** Display the loading/character selection screen. */
    public void showLoadingScreen() {
        System.out.println("=== Turn-Based Combat Arena ===");
        System.out.println("Choose your player:");
        System.out.println("  1) Warrior (HP:260 ATK:40 DEF:20 SPD:30)");
        System.out.println("  2) Wizard  (HP:200 ATK:50 DEF:10 SPD:20)");
        System.out.println();
        System.out.println("Choose 2 items (duplicates allowed):");
        System.out.println("  - Potion");
        System.out.println("  - Power Stone");
        System.out.println("  - Smoke Bomb");
        System.out.println();
        System.out.println("Choose difficulty:");
        System.out.println("  1) Easy");
        System.out.println("  2) Medium");
        System.out.println("  3) Hard");
    }

    /** Display battle status at start of each round. */
    public void showRoundStart(int roundNumber, Combatant player, List<Combatant> enemies) {
        System.out.println("\n--- Round " + roundNumber + " ---");
        System.out.println("Player: " + BattleDisplay.combatantSummary(player));
        System.out.println("Enemies:");
        for (Combatant enemy : enemies) {
            System.out.println("  - " + BattleDisplay.combatantSummary(enemy));
        }
    }

    /** Prompt player to choose an action. Return the index of chosen action. */
    public int promptActionChoice(List<String> actionNames) {
        System.out.println("\nChoose action:");
        for (int i = 0; i < actionNames.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + actionNames.get(i));
        }
        return inputHandler.promptIndex("Action", 1, actionNames.size()) - 1;
    }

    /** Prompt player to choose a target enemy. */
    public int promptTargetChoice(List<Combatant> enemies) {
        System.out.println("\nChoose target:");
        for (int i = 0; i < enemies.size(); i++) {
            Combatant enemy = enemies.get(i);
            String status = enemy.isAlive() ? "Alive" : "Eliminated";
            System.out.println("  " + (i + 1) + ") " + enemy.getName() + " (" + status + ")");
        }
        return inputHandler.promptIndex("Target", 1, enemies.size()) - 1;
    }

    /** Display the result of an action. */
    public void showActionResult(String message) {
        System.out.println(message);
    }

    /** Display victory screen. */
    public void showVictoryScreen(int remainingHP, int maxHP, int totalRounds) {
        System.out.println("\n=== Victory ===");
        System.out.println("Congratulations, you defeated all enemies!");
        System.out.println("Statistics:");
        System.out.println("Remaining HP: " + remainingHP + "/" + maxHP);
        System.out.println("Total Rounds: " + totalRounds);
        System.out.println("Choose next: replay / new game / exit");
    }

    /** Display defeat screen. */
    public void showDefeatScreen(int enemiesRemaining, int totalRounds) {
        System.out.println("\n=== Defeat ===");
        System.out.println("Defeated. Do not give up, try again!");
        System.out.println("Statistics:");
        System.out.println("Enemies Remaining: " + enemiesRemaining);
        System.out.println("Total Rounds Survived: " + totalRounds);
        System.out.println("Choose next: replay / new game / exit");
    }
}
