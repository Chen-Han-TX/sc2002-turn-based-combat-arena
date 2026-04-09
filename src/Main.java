import engine.BattleEngine;
import engine.SpeedBasedOrder;
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

        while (running) {
            ui.showLoadingScreen();

            Combatant player = createPlayer(ui.promptPlayerChoice());
            List<Item> items = createItemsFromChoices(
                    ui.promptItemChoices(List.of("Potion", "Power Stone", "Smoke Bomb"), 2)
            );

            int difficulty = ui.promptDifficultyChoice();

            List<Combatant> initialEnemies = createInitialEnemies(difficulty);
            List<Combatant> backupEnemies = createBackupEnemies(difficulty);

            BattleEngine engine = new BattleEngine(
                    player,
                    initialEnemies,
                    backupEnemies,
                    items,
                    new SpeedBasedOrder(),
                    ui
            );

            engine.startBattle();

            int nextChoice = ui.promptPostGameChoice();

            if (nextChoice == 0) {
                // replay same settings
                BattleEngine replayEngine = new BattleEngine(
                        recreatePlayer(player),
                        recreateEnemies(initialEnemies),
                        recreateEnemies(backupEnemies),
                        recreateItems(items),
                        new SpeedBasedOrder(),
                        ui
                );
                replayEngine.startBattle();

                int afterReplay = ui.promptPostGameChoice();
                if (afterReplay == 1) {
                    continue;
                } else if (afterReplay == 2) {
                    running = false;
                }
            } else if (nextChoice == 1) {
                continue;
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

    private static Combatant recreatePlayer(Combatant player) {
        if (player instanceof Warrior) {
            return new Warrior();
        } else if (player instanceof Wizard) {
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

    private static List<Item> recreateItems(List<Item> oldItems) {
        List<Item> items = new ArrayList<>();

        for (Item item : oldItems) {
            if (item instanceof Potion) {
                items.add(new Potion());
            } else if (item instanceof PowerStone) {
                items.add(new PowerStone());
            } else if (item instanceof SmokeBomb) {
                items.add(new SmokeBomb());
            }
        }

        return items;
    }

    private static List<Combatant> createInitialEnemies(int difficulty) {
        List<Combatant> enemies = new ArrayList<>();

        switch (difficulty) {
            case 0: // Easy
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                enemies.add(new Goblin("Goblin C"));
                break;

            case 1: // Medium
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Wolf("Wolf A"));
                break;

            case 2: // Hard
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
            case 0: // Easy
                break;

            case 1: // Medium
                backup.add(new Wolf("Wolf B"));
                backup.add(new Wolf("Wolf C"));
                break;

            case 2: // Hard
                backup.add(new Goblin("Goblin C"));
                backup.add(new Wolf("Wolf A"));
                backup.add(new Wolf("Wolf B"));
                break;

            default:
                break;
        }

        return backup;
    }

    private static List<Combatant> recreateEnemies(List<Combatant> oldEnemies) {
        List<Combatant> newEnemies = new ArrayList<>();

        for (Combatant enemy : oldEnemies) {
            if (enemy instanceof Goblin) {
                newEnemies.add(new Goblin(enemy.getName()));
            } else if (enemy instanceof Wolf) {
                newEnemies.add(new Wolf(enemy.getName()));
            }
        }

        return newEnemies;
    }
}