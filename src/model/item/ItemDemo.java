package model.item;

import model.combatant.*;
import model.effect.StatusEffect;

import java.util.ArrayList;
import java.util.List;

public class ItemDemo {
    public static void main(String[] args) {

        // -------------------------
        // 1. Potion test
        // -------------------------
        System.out.println("=== Potion Test ===");
        Warrior warrior = new Warrior();
        warrior.takeRawDamage(120); // 260 -> 140

        System.out.println("Before Potion HP = " + warrior.getCurrentHP());

        Potion potion = new Potion();
        potion.use(warrior, null, new ArrayList<>());

        System.out.println("After Potion HP = " + warrior.getCurrentHP());
        System.out.println("Potion consumed = " + potion.isConsumed());

        // -------------------------
        // 2. Smoke Bomb test
        // -------------------------
        System.out.println("\n=== Smoke Bomb Test ===");
        Warrior warrior2 = new Warrior();

        SmokeBomb smokeBomb = new SmokeBomb();
        smokeBomb.use(warrior2, null, new ArrayList<>());

        System.out.println("Smoke Bomb consumed = " + smokeBomb.isConsumed());
        System.out.println("Active effects after Smoke Bomb:");

        for (StatusEffect effect : warrior2.getStatusEffects()) {
            System.out.println("- " + effect.getName());
        }

        // -------------------------
        // 3. Power Stone + Arcane Blast test
        // -------------------------
        System.out.println("\n=== Power Stone Test ===");
        Wizard wizard = new Wizard();

        List<Combatant> enemies = new ArrayList<>();
        Goblin g1 = new Goblin("Goblin A");
        Goblin g2 = new Goblin("Goblin B");

        // weaken them so Arcane Blast kills both
        g1.takeRawDamage(40); // 55 -> 15
        g2.takeRawDamage(40); // 55 -> 15

        enemies.add(g1);
        enemies.add(g2);

        System.out.println("Before Power Stone ATK = " + wizard.getAttack());

        PowerStone ps = new PowerStone();
        ps.use(wizard, null, enemies);

        System.out.println("After Power Stone ATK = " + wizard.getAttack());
        System.out.println("Power Stone consumed = " + ps.isConsumed());
        System.out.println("Goblin A alive: " + g1.isAlive());
        System.out.println("Goblin B alive: " + g2.isAlive());

        // -------------------------
        // 4. StunEffect test
        // -------------------------
        System.out.println("\n=== StunEffect Test ===");
        model.effect.StunEffect stun = new model.effect.StunEffect(2);

        System.out.println("Stun prevents action: " + stun.preventsAction());
        System.out.println("Expired at start: " + stun.isExpired());

        stun.tick();
        System.out.println("Expired after 1 tick: " + stun.isExpired());

        stun.tick();
        System.out.println("Expired after 2 ticks: " + stun.isExpired());
    }
}