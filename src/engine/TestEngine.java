package engine;

import model.combatant.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Person D: Run this to test your engine classes.
 * 
 * How to run:
 *   cd src
 *   javac engine/TestEngine.java
 *   java engine.TestEngine
 */
public class TestEngine {

    static int passed = 0;
    static int failed = 0;

    static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName);
            failed++;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Testing Engine ===\n");

        // --- SpeedBasedOrder ---
        System.out.println("[SpeedBasedOrder]");
        SpeedBasedOrder sbo = new SpeedBasedOrder();

        Warrior w = new Warrior();    // SPD: 30
        Goblin g = new Goblin("Goblin A");  // SPD: 25
        Wolf wolf = new Wolf("Wolf A");      // SPD: 35

        List<Combatant> combatants = new ArrayList<>();
        combatants.add(w);
        combatants.add(g);
        combatants.add(wolf);

        List<Combatant> ordered = sbo.determineTurnOrder(combatants);
        check("Wolf (35) goes first", ordered.get(0).getName().equals("Wolf A"));
        check("Warrior (30) goes second", ordered.get(1).getName().equals("Warrior"));
        check("Goblin (25) goes third", ordered.get(2).getName().equals("Goblin A"));

        // Test with Wizard
        System.out.println("\n[SpeedBasedOrder - Wizard scenario]");
        Wizard wiz = new Wizard();  // SPD: 20
        List<Combatant> combatants2 = new ArrayList<>();
        combatants2.add(wiz);
        combatants2.add(g);
        combatants2.add(wolf);

        List<Combatant> ordered2 = sbo.determineTurnOrder(combatants2);
        check("Wolf (35) first", ordered2.get(0).getName().equals("Wolf A"));
        check("Goblin (25) second", ordered2.get(1).getName().equals("Goblin A"));
        check("Wizard (20) third", ordered2.get(2).getName().equals("Wizard"));

        // --- BattleEngine basic checks ---
        System.out.println("\n[BattleEngine - Setup]");
        List<Combatant> enemies = new ArrayList<>();
        enemies.add(new Goblin("Goblin A"));
        enemies.add(new Goblin("Goblin B"));

        BattleEngine engine = new BattleEngine(new Warrior(), enemies, null, new SpeedBasedOrder());
        check("Round starts at 0", engine.getRoundNumber() == 0);
        check("Player is not defeated", !engine.isPlayerDefeated());
        check("Player has not won yet", !engine.isPlayerVictory());

        // Simulate all enemies dead
        System.out.println("\n[BattleEngine - Win condition]");
        List<Combatant> enemies2 = new ArrayList<>();
        Goblin deadG = new Goblin("Dead Goblin");
        deadG.takeRawDamage(999);
        enemies2.add(deadG);

        BattleEngine engine2 = new BattleEngine(new Warrior(), enemies2, null, new SpeedBasedOrder());
        check("All enemies dead = player victory", engine2.isPlayerVictory());

        // Simulate player dead
        System.out.println("\n[BattleEngine - Lose condition]");
        Warrior deadW = new Warrior();
        deadW.takeRawDamage(999);
        List<Combatant> enemies3 = new ArrayList<>();
        enemies3.add(new Goblin("Goblin"));

        BattleEngine engine3 = new BattleEngine(deadW, enemies3, null, new SpeedBasedOrder());
        check("Player dead = player defeated", engine3.isPlayerDefeated());

        // --- Level config ---
        // TODO: Uncomment when you implement Level.java
        /*
        System.out.println("\n[Level]");
        Level easy = Level.EASY;
        check("Easy has 3 initial enemies", easy.getInitialSpawn().size() == 3);
        check("Easy has no backup", easy.getBackupSpawn().isEmpty());

        Level medium = Level.MEDIUM;
        check("Medium has 2 initial enemies", medium.getInitialSpawn().size() == 2);
        check("Medium has 2 backup", medium.getBackupSpawn().size() == 2);
        */

        // --- Summary ---
        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }
}
