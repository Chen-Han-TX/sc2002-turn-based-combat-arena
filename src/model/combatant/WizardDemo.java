package model.combatant;

import java.util.ArrayList;
import java.util.List;

public class WizardDemo {
    public static void main(String[] args) {
        Wizard wizard = new Wizard();

        List<Combatant> enemies = new ArrayList<>();
        Goblin g1 = new Goblin("Goblin A");
        Goblin g2 = new Goblin("Goblin B");

        // weaken enemies so Arcane Blast will kill them
        g1.takeRawDamage(40); // 55 -> 15
        g2.takeRawDamage(40); // 55 -> 15

        enemies.add(g1);
        enemies.add(g2);

        System.out.println("Before Arcane Blast ATK = " + wizard.getAttack());

        wizard.useArcaneBlast(enemies);

        System.out.println("After Arcane Blast ATK = " + wizard.getAttack());
        System.out.println("Goblin A alive: " + g1.isAlive());
        System.out.println("Goblin B alive: " + g2.isAlive());
    }
}