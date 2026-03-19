import model.combatant.*;
import model.action.*;
import java.util.List;
import java.util.ArrayList;

/**
 * INTEGRATION TEST — Run this together to verify the game flow
 * matches Appendix A (Easy difficulty example).
 * 
 * This doesn't need a full engine — it manually walks through
 * the first few rounds to verify damage math is correct.
 * 
 * How to run:
 *   cd src
 *   javac TestGameFlow.java
 *   java TestGameFlow
 */
public class TestGameFlow {

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
        System.out.println("=== Integration Test: Easy Level (Appendix A) ===\n");
        System.out.println("Warrior (HP:260 ATK:40 DEF:20 SPD:30)");
        System.out.println("vs 3 Goblins (HP:55 ATK:35 DEF:15 SPD:25)\n");

        Warrior warrior = new Warrior();
        Goblin gA = new Goblin("Goblin A");
        Goblin gB = new Goblin("Goblin B");
        Goblin gC = new Goblin("Goblin C");
        BasicAttack attack = new BasicAttack();
        List<Combatant> enemies = new ArrayList<>(List.of(gA, gB, gC));

        // === ROUND 1 ===
        System.out.println("--- Round 1 ---");

        // Warrior attacks Goblin A: dmg = 40-15 = 25, HP: 55->30
        attack.execute(warrior, gA, enemies);
        check("Warrior -> Goblin A: HP 55 -> 30", gA.getCurrentHP() == 30);

        // Goblin A attacks Warrior: dmg = 35-20 = 15, HP: 260->245
        attack.execute(gA, warrior, enemies);
        check("Goblin A -> Warrior: HP 260 -> 245", warrior.getCurrentHP() == 245);

        // Goblin B attacks Warrior: HP: 245->230
        attack.execute(gB, warrior, enemies);
        check("Goblin B -> Warrior: HP 245 -> 230", warrior.getCurrentHP() == 230);

        // Goblin C attacks Warrior: HP: 230->215
        attack.execute(gC, warrior, enemies);
        check("Goblin C -> Warrior: HP 230 -> 215", warrior.getCurrentHP() == 215);

        System.out.println("  End of Round 1: Warrior HP=" + warrior.getCurrentHP()
            + " | GA=" + gA.getCurrentHP()
            + " | GB=" + gB.getCurrentHP()
            + " | GC=" + gC.getCurrentHP());

        // === ROUND 3 (skip R2 for brevity, test elimination) ===
        System.out.println("\n--- Elimination test ---");

        // Hit Goblin A twice more (each 25 dmg) to eliminate
        // Current HP: 30, after hit: 5, after another hit: 0
        attack.execute(warrior, gA, enemies);
        check("Goblin A hit again: HP 30 -> 5", gA.getCurrentHP() == 5);

        attack.execute(warrior, gA, enemies);
        check("Goblin A eliminated: HP 5 -> 0", gA.getCurrentHP() == 0);
        check("Goblin A is not alive", !gA.isAlive());

        // === Wizard vs Wolf scenario (from Appendix A iii) ===
        System.out.println("\n--- Wizard vs Wolf damage check ---");
        Wizard wizard = new Wizard();  // ATK:50 DEF:10
        Wolf wolf = new Wolf("Wolf");  // ATK:45 DEF:5 HP:40

        // Wolf attacks Wizard: dmg = 45-10 = 35, HP: 200->165
        attack.execute(wolf, wizard, enemies);
        check("Wolf -> Wizard: HP 200 -> 165 (dmg 35)", wizard.getCurrentHP() == 165);

        // Wizard attacks Wolf: dmg = 50-5 = 45, HP: 40->0 (eliminated!)
        attack.execute(wizard, wolf, enemies);
        check("Wizard -> Wolf: HP 40 -> 0 (dmg 45, overkill)", wolf.getCurrentHP() == 0);
        check("Wolf eliminated", !wolf.isAlive());

        // === Summary ===
        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");

        if (failed == 0) {
            System.out.println("\nAll damage math matches Appendix A! Your BasicAttack logic is correct.");
        } else {
            System.out.println("\nSome tests failed — check your damage formula:");
            System.out.println("  damage = max(0, attacker.getAttack() - target.getDefense())");
            System.out.println("  target HP = max(0, currentHP - damage)");
        }
    }
}
