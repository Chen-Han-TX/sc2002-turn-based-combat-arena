package ui;

import model.combatant.Combatant;
import model.action.Action;
import model.item.Item;
import java.util.List;
import java.util.Scanner;

/**
 * Owner: Person E
 * Handles all CLI display and user input.
 * Separated from battle logic (UI should not contain game rules).
 */
public class GameUI {
    private Scanner scanner;

    public GameUI() {
        this.scanner = new Scanner(System.in);
    }

    /** Display the loading/character selection screen. */
    public void showLoadingScreen() {
        // TODO: Show player options (Warrior/Wizard), item selection, difficulty
    }

    /** Display battle status at start of each round. */
    public void showRoundStart(int roundNumber, Combatant player, List<Combatant> enemies) {
        // TODO: Show round number, HP of all combatants, active effects
    }

    /** Prompt player to choose an action. Return the index of chosen action. */
    public int promptActionChoice(List<String> actionNames) {
        // TODO: Display numbered menu, read input, validate
        return 0;
    }

    /** Prompt player to choose a target enemy. */
    public int promptTargetChoice(List<Combatant> enemies) {
        // TODO: Display alive enemies, read input, validate
        return 0;
    }

    /** Display the result of an action. */
    public void showActionResult(String message) {
        System.out.println(message);
    }

    /** Display victory screen. */
    public void showVictoryScreen(int remainingHP, int maxHP, int totalRounds) {
        // TODO: Congratulations, stats, replay option
    }

    /** Display defeat screen. */
    public void showDefeatScreen(int enemiesRemaining, int totalRounds) {
        // TODO: Defeat message, stats, replay option
    }
}
