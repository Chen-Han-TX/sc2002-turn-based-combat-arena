package model.item;

import model.combatant.*;
import model.effect.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Person C: Run this to test your item and effect classes.
 * 
 * How to run:
 *   cd src
 *   javac model/item/TestItemsAndEffects.java
 *   java model.item.TestItemsAndEffects
 */
public class TestItemsAndEffects {

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
        System.out.println("=== Testing Items & Effects ===\n");

        // --- Potion ---
        System.out.println("[Potion]");
        Potion pot = new Potion();
        check("Name is Potion", pot.getName().equals("Potion"));
        check("Not consumed initially", !pot.isConsumed());

        Warrior w = new Warrior();  // HP: 260
        w.takeRawDamage(120);       // HP: 140
        pot.use(w, null, new ArrayList<>());
        check("Heal 100: 140 -> 240", w.getCurrentHP() == 240);
        check("Potion consumed after use", pot.isConsumed());

        // Potion should not exceed max HP
        Warrior w2 = new Warrior();  // HP: 260
        w2.takeRawDamage(30);        // HP: 230
        Potion pot2 = new Potion();
        pot2.use(w2, null, new ArrayList<>());
        check("Heal capped at max HP: 230 -> 260 (not 330)", w2.getCurrentHP() == 260);

        // --- SmokeBomb ---
        // TODO: Uncomment when you implement SmokeBomb.java + SmokeBombEffect.java
        /*
        System.out.println("\n[SmokeBomb]");
        SmokeBomb sb = new SmokeBomb();
        Warrior w3 = new Warrior();
        sb.use(w3, null, new ArrayList<>());
        check("SmokeBomb consumed", sb.isConsumed());
        check("Player has SmokeBomb effect",
            w3.getStatusEffects().stream().anyMatch(e -> e.getName().contains("Smoke")));
        */

        // --- PowerStone ---
        // TODO: Uncomment when you implement PowerStone.java
        /*
        System.out.println("\n[PowerStone]");
        PowerStone ps = new PowerStone();
        check("Not consumed initially", !ps.isConsumed());
        // PowerStone triggers special skill — needs more integration to test
        // For now, just check it consumes itself
        */

        // --- StunEffect ---
        // TODO: Uncomment when you implement StunEffect.java
        /*
        System.out.println("\n[StunEffect]");
        StunEffect stun = new StunEffect(2);  // lasts 2 turns
        check("Stun prevents action", stun.preventsAction());
        check("Not expired at start", !stun.isExpired());

        stun.tick();  // turn 1 done
        check("Not expired after 1 tick", !stun.isExpired());

        stun.tick();  // turn 2 done
        check("Expired after 2 ticks", stun.isExpired());
        */

        // --- DefendBuff ---
        // TODO: Uncomment when you implement DefendBuff.java
        /*
        System.out.println("\n[DefendBuff]");
        Warrior w4 = new Warrior();  // DEF: 20
        DefendBuff db = new DefendBuff(2);  // lasts 2 turns
        w4.addStatusEffect(db);
        db.applyEffect(w4);  // should add +10 defense
        // Note: depending on your design, applyEffect might be called
        // once on creation or each turn — adjust test accordingly

        check("DefendBuff does not prevent action", !db.preventsAction());
        db.tick();
        db.tick();
        check("DefendBuff expired after 2 ticks", db.isExpired());
        db.onExpire(w4);
        check("Defense restored to 20 after expire", w4.getDefense() == 20);
        */

        // --- Summary ---
        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }
}
