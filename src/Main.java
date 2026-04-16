import engine.BattleEngine;
import engine.SpeedBasedOrder;
import engine.SurvivalGameRunner;
import model.combatant.Combatant;
import model.combatant.Giant;
import model.combatant.Goblin;
import model.combatant.Warrior;
import model.combatant.Wizard;
import model.combatant.Wolf;
import model.item.Item;
import model.item.Potion;
import model.item.PowerStone;
import model.item.SmokeBomb;
import ui.GameUI;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("=== Turn-Based Combat Arena ===");
            System.out.println("Run with --interactive to start the playable CLI version.");
            return;
        }

        if ("--interactive".equals(args[0])) {
            runInteractiveGame();
        } else {
            System.out.println("Unknown option.");
            System.out.println("Use --interactive to start the game.");
        }
    }

    private static void runInteractiveGame() {
        GameUI ui = new GameUI();

        boolean running = true;
        boolean replayRequested = false;

        // Saved choices for replay — null until a game has been completed
        Integer savedMode = null;
        Integer savedPlayerChoice = null;
        List<Integer> savedItemChoices = null;
        Integer savedDifficulty = null;

        while (running) {
            ui.showLoadingScreen();

            int modeChoice;
            int playerChoice;
            List<Integer> itemChoices;
            int difficulty = -1;

            if (replayRequested && savedMode != null) {
                // Re-use the saved choices without prompting the player again
                modeChoice = savedMode;
                playerChoice = savedPlayerChoice;
                itemChoices = new ArrayList<>(savedItemChoices);
                if (savedDifficulty != null) {
                    difficulty = savedDifficulty;
                }
                replayRequested = false;
                ui.showActionResult("Replaying with the same settings...");
            } else {
                modeChoice = ui.promptGameMode();
                playerChoice = ui.promptPlayerChoice();
                itemChoices = ui.promptItemChoices(List.of("Potion", "Power Stone", "Smoke Bomb"), 2);
            }

            Combatant player = createPlayer(playerChoice);
            List<Item> items = createItemsFromChoices(itemChoices);

            if (modeChoice == 2) {
                SurvivalGameRunner survivalRunner = new SurvivalGameRunner(
                        player,
                        items,
                        new SpeedBasedOrder(),
                        ui
                );
                survivalRunner.start();
            } else {
                if (difficulty == -1) {
                    difficulty = ui.promptDifficultyChoice();
                }

                List<Combatant> initialEnemies = createInitialEnemies(difficulty);
                List<Combatant> backupEnemies = createBackupEnemies(difficulty);

                BattleEngine engine = new BattleEngine(
                        player,
                        initialEnemies,
                        backupEnemies,
                        items,
                        new SpeedBasedOrder(),
                        ui,
                        true
                );

                engine.startBattle();
            }

            // Persist choices so replay can reuse them
            savedMode = modeChoice;
            savedPlayerChoice = playerChoice;
            savedItemChoices = new ArrayList<>(itemChoices);
            savedDifficulty = (modeChoice != 2) ? difficulty : null;

            int nextChoice = ui.promptPostGameChoice();

            if (nextChoice == 0) {
                replayRequested = true;   // Replay same settings
            } else if (nextChoice == 1) {
                // New game — fall through to re-prompt at top of loop
            } else {
                running = false;
            }
        }

        System.out.println("Thanks for playing!");
    }

    private static Combatant createPlayer(int choice) {
        if (choice == 0) {
            return new Warrior();
        } else if (choice == 1) {
            return new Wizard();
        }
        return new Giant();
    }

    private static List<Item> createItemsFromChoices(List<Integer> choices) {
        List<Item> items = new ArrayList<>();

        for (int choice : choices) {
            switch (choice) {
                case 0:
                    items.add(new Potion());
                    break;
                case 1:
                    items.add(new PowerStone());
                    break;
                case 2:
                    items.add(new SmokeBomb());
                    break;
                default:
                    break;
            }
        }

        return items;
    }

    private static List<Combatant> createInitialEnemies(int difficulty) {
        List<Combatant> enemies = new ArrayList<>();

        switch (difficulty) {
            case 0:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                enemies.add(new Goblin("Goblin C"));
                break;
            case 1:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Wolf("Wolf A"));
                break;
            case 2:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                break;
            default:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                enemies.add(new Goblin("Goblin C"));
                break;
        }

        return enemies;
    }

    private static List<Combatant> createBackupEnemies(int difficulty) {
        List<Combatant> backup = new ArrayList<>();

        switch (difficulty) {
            case 0:
                break;
            case 1:
                backup.add(new Wolf("Wolf B"));
                backup.add(new Wolf("Wolf C"));
                break;
            case 2:
                backup.add(new Goblin("Goblin C"));
                backup.add(new Wolf("Wolf A"));
                backup.add(new Wolf("Wolf B"));
                break;
            default:
                break;
        }

        return backup;
    }
}