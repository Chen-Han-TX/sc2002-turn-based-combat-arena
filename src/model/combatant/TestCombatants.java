package model.combatant;

/**
 * Person A: Run this to test your combatant classes.
 * 
 * How to run:
 *   cd src
 *   javac model/combatant/TestCombatants.java
 *   java model.combatant.TestCombatants
 */
public class TestCombatants {

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
        System.out.println("=== Testing Combatants ===\n");

        // --- Warrior tests ---
        System.out.println("[Warrior]");
        Warrior w = new Warrior();
        check("Name is Warrior", w.getName().equals("Warrior"));
        check("HP is 260", w.getMaxHP() == 260);
        check("Attack is 40", w.getAttack() == 40);
        check("Defense is 20", w.getDefense() == 20);
        check("Speed is 30", w.getSpeed() == 30);
        check("Is a player", w.isPlayer());
        check("Starts alive", w.isAlive());

        // --- Wizard tests ---
        System.out.println("\n[Wizard]");
        Wizard wiz = new Wizard();
        check("HP is 200", wiz.getMaxHP() == 200);
        check("Attack is 50", wiz.getAttack() == 50);
        check("Defense is 10", wiz.getDefense() == 10);
        check("Speed is 20", wiz.getSpeed() == 20);

        // --- Goblin tests ---
        System.out.println("\n[Goblin]");
        Goblin g = new Goblin("Goblin A");
        check("Name is Goblin A", g.getName().equals("Goblin A"));
        check("HP is 55", g.getMaxHP() == 55);
        check("Attack is 35", g.getAttack() == 35);
        check("Not a player", !g.isPlayer());

        // --- Wolf tests ---
        System.out.println("\n[Wolf]");
        Wolf wolf = new Wolf("Wolf A");
        check("HP is 40", wolf.getMaxHP() == 40);
        check("Attack is 45", wolf.getAttack() == 45);
        check("Defense is 5", wolf.getDefense() == 5);
        check("Speed is 35", wolf.getSpeed() == 35);

        // --- Damage tests ---
        System.out.println("\n[Damage & Healing]");
        Warrior w2 = new Warrior();  // HP:260, DEF:20
        w2.takeRawDamage(50);
        check("Take 50 raw damage -> HP 210", w2.getCurrentHP() == 210);

        w2.heal(30);
        check("Heal 30 -> HP 240", w2.getCurrentHP() == 240);

        w2.heal(999);
        check("Heal can't exceed max HP -> HP 260", w2.getCurrentHP() == 260);

        w2.takeRawDamage(999);
        check("Massive damage -> HP clamped to 0", w2.getCurrentHP() == 0);
        check("HP 0 means not alive", !w2.isAlive());

        // --- Stat modifier tests ---
        System.out.println("\n[Stat Modifiers]");
        Warrior w3 = new Warrior();
        w3.modifyDefense(10);
        check("Defense +10 -> 30", w3.getDefense() == 30);
        w3.modifyDefense(-10);
        check("Defense -10 -> back to 20", w3.getDefense() == 20);
        w3.modifyAttack(10);
        check("Attack +10 -> 50", w3.getAttack() == 50);

        // --- Summary ---
        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }
}
