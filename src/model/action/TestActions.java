package model.action;

import model.combatant.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Person B: Run this to test your action classes.
 * 
 * How to run:
 *   cd src
 *   javac model/action/TestActions.java
 *   java model.action.TestActions
 */
public class TestActions {

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
        System.out.println("=== Testing Actions ===\n");

        // --- BasicAttack ---
        System.out.println("[BasicAttack]");
        BasicAttack ba = new BasicAttack();
        check("Name is Basic Attack", ba.getName().equals("Basic Attack"));
        check("Always available", ba.isAvailable(new Warrior()));

        // Warrior (ATK:40) attacks Goblin (DEF:15) -> damage = 40-15 = 25
        Warrior w = new Warrior();
        Goblin g = new Goblin("Goblin A");
        List<Combatant> enemies = new ArrayList<>();
        enemies.add(g);

        int hpBefore = g.getCurrentHP();  // 55
        ba.execute(w, g, enemies);
        int hpAfter = g.getCurrentHP();
        check("Warrior hits Goblin: 55 -> 30 (dmg 25)", hpAfter == 30);

        // Goblin (ATK:35) attacks Warrior (DEF:20) -> damage = 35-20 = 15
        Warrior w2 = new Warrior();
        ba.execute(g, w2, enemies);
        check("Goblin hits Warrior: 260 -> 245 (dmg 15)", w2.getCurrentHP() == 245);

        // Wolf (ATK:45) attacks Wizard (DEF:10) -> damage = 45-10 = 35
        Wolf wolf = new Wolf("Wolf");
        Wizard wiz = new Wizard();
        ba.execute(wolf, wiz, enemies);
        check("Wolf hits Wizard: 200 -> 165 (dmg 35)", wiz.getCurrentHP() == 165);

        // Edge case: attack should not go below 0 damage
        // (e.g. if target defense > attacker attack somehow)
        System.out.println("\n[BasicAttack - Edge Cases]");
        Warrior tank = new Warrior();
        tank.modifyDefense(100);  // DEF now 120
        Goblin weakG = new Goblin("Weak Goblin");  // ATK 35
        int tankHpBefore = tank.getCurrentHP();
        ba.execute(weakG, tank, enemies);
        check("No negative damage (DEF > ATK)", tank.getCurrentHP() == tankHpBefore);

        // --- Defend ---
        // TODO: Uncomment when you implement Defend.java
        /*
        System.out.println("\n[Defend]");
        Defend defend = new Defend();
        Warrior w3 = new Warrior();  // DEF: 20
        defend.execute(w3, null, enemies);
        check("Defend adds +10 defense -> 30", w3.getDefense() == 30);
        */

        // --- ShieldBash ---
        // TODO: Uncomment when you implement ShieldBash.java
        /*
        System.out.println("\n[ShieldBash]");
        ShieldBash sb = new ShieldBash();
        Warrior w4 = new Warrior();
        Goblin g2 = new Goblin("Goblin B");
        sb.execute(w4, g2, enemies);
        check("ShieldBash deals damage: 55 -> 30", g2.getCurrentHP() == 30);
        check("ShieldBash stuns target", 
            g2.getStatusEffects().stream().anyMatch(e -> e.getName().equals("Stun")));
        */

        // --- ArcaneBlast ---
        // TODO: Uncomment when you implement ArcaneBlast.java
        /*
        System.out.println("\n[ArcaneBlast]");
        ArcaneBlast ab = new ArcaneBlast();
        Wizard wiz2 = new Wizard();  // ATK: 50
        Goblin g3 = new Goblin("Goblin C");  // HP:55, DEF:15 -> dmg 35
        Wolf wolf2 = new Wolf("Wolf B");      // HP:40, DEF:5  -> dmg 45 -> eliminated
        List<Combatant> enemyList = new ArrayList<>();
        enemyList.add(g3);
        enemyList.add(wolf2);
        ab.execute(wiz2, null, enemyList);
        check("Arcane Blast hits Goblin: 55 -> 20", g3.getCurrentHP() == 20);
        check("Arcane Blast kills Wolf: 40 -> 0", wolf2.getCurrentHP() == 0);
        check("Wizard ATK +10 per kill -> 60", wiz2.getAttack() == 60);
        */

        // --- Summary ---
        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }
}
