package ui;

import model.combatant.Combatant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("  3) Giant   (HP:400 ATK:35 DEF:20 SPD:10)");
        System.out.println();
        System.out.println("Enemies:");
        System.out.println("  - Goblin (HP:55 ATK:35 DEF:15 SPD:25)");
        System.out.println("  - Wolf (HP:40 ATK:45 DEF:5 SPD:35)");
        System.out.println();
        System.out.println("Choose 2 items (duplicates allowed):");
        System.out.println("  - Potion");
        System.out.println("  - Power Stone");
        System.out.println("  - Smoke Bomb");
        System.out.println();
        System.out.println("Choose difficulty:");
        System.out.println("  1) Easy  — 3 Goblins");
        System.out.println("  2) Medium — 1 Goblin + 1 Wolf, Backup Spawn: 2 Wolves");
        System.out.println("  3) Hard  — 2 Goblins, Backup Spawn: 1 Goblin + 2 Wolves");
    }

    /** Prompt and return selected game mode: 1=Classic, 2=Survival. */
    public int promptGameMode() {
        System.out.println("\nChoose game mode:");
        System.out.println("  1) Classic Mode");
        System.out.println("  2) Survival Mode");
        return inputHandler.promptIndex("Mode", 1, 2);
    }

    /** Prompt and return selected player **/
    public int promptPlayerChoice() {
        System.out.println("\nSelect your player:");
        System.out.println("  1) Warrior");
        System.out.println("  2) Wizard");
        System.out.println("  3) Giant");
        return inputHandler.promptIndex("Player", 1, 3) - 1;
    }

    /**
     * Prompt and return selected item indices
     */
    public List<Integer> promptItemChoices(List<String> itemNames, int picks) {
        List<Integer> selected = new ArrayList<>();
        for (int pick = 1; pick <= picks; pick++) {
            System.out.println("\nSelect item " + pick + "/" + picks + ":");
            for (int i = 0; i < itemNames.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + itemNames.get(i));
            }
            selected.add(inputHandler.promptIndex("Item", 1, itemNames.size()) - 1);
        }
        return selected;
    }

    /** Prompt and return selected difficulty index: 0=Easy, 1=Medium, 2=Hard. */
    public int promptDifficultyChoice() {
        System.out.println("\nSelect difficulty:");
        System.out.println("  1) Easy");
        System.out.println("  2) Medium");
        System.out.println("  3) Hard");
        return inputHandler.promptIndex("Difficulty", 1, 3) - 1;
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

    /** Display end-of-round summary for all combatants. */
    public void showRoundEnd(Combatant player, List<Combatant> enemies) {
        System.out.println("\n-- End of Round --");
        System.out.println("Player: " + BattleDisplay.combatantSummary(player));
        for (Combatant enemy : enemies) {
            System.out.println("  " + BattleDisplay.combatantSummary(enemy));
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

    public int promptActionChoice(List<String> actionNames, int cooldown, int itemsRemaining) {
        System.out.println("\nCooldown: " + (cooldown > 0 ? cooldown + " turn(s)" : "Ready"));
        System.out.println("Items remaining: " + itemsRemaining);
        return promptActionChoice(actionNames);
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

    /** victory screen. */
    public void showVictoryScreen(int remainingHP, int maxHP, int totalRounds) {
        System.out.println("\n=== Victory ===");
        System.out.println("Congratulations, you have defeated all your enemies.");
        System.out.println("Statistics: Remaining HP: " + remainingHP + "/" + maxHP
                + " | Total Rounds: " + totalRounds);
        System.out.println("Options: replay same settings / start a new game / exit");
    }

    /** defeat screen. */
    public void showDefeatScreen(int enemiesRemaining, int totalRounds) {
        System.out.println("\n=== Defeat ===");
        System.out.println("Defeated. Don't give up, try again!");
        System.out.println("Statistics: Enemies remaining: " + enemiesRemaining
                + " | Total Rounds Survived: " + totalRounds);
        System.out.println("Options: replay same settings / start a new game / exit");
    }

    /**
     * Prompt and return post-game option.
     * Returns: 0 = Replay same settings, 1 = New game, 2 = Exit.
     */
    public int promptPostGameChoice() {
        System.out.println("\nChoose next:");
        System.out.println("  1) Replay same settings");
        System.out.println("  2) New game");
        System.out.println("  3) Exit");
        return inputHandler.promptIndex("Menu", 1, 3) - 1;
    }
}
