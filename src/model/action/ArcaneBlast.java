package model.action;

import model.combatant.Combatant;
import model.effect.ArcaneBlastEffect;

import java.util.List;

/**
 * Wizard special skill: Arcane Blast.
 * Hits all living enemies for the performer's full attack value.
 * Each enemy defeated grants the performer +10 attack (via ArcaneBlastEffect).
 */
public class ArcaneBlast implements Action {

    @Override
    public String getName() {
        return "Arcane Blast";
    }

    @Override
    public boolean needsTarget() {
        return false;
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {
        int enemiesDefeated = 0;

        System.out.println(performer.getName() + " casts Arcane Blast!");

        for (Combatant enemy : allEnemies) {
            if (enemy != null && enemy.isAlive()) {
                int beforeHp = enemy.getCurrentHP();
                enemy.takeDamage(performer.getAttack());
                int actualDamage = beforeHp - enemy.getCurrentHP();

                System.out.println("Blast hit " + enemy.getName()
                        + " for " + actualDamage + " damage.");

                if (!enemy.isAlive()) {
                    enemiesDefeated++;
                    System.out.println(enemy.getName() + " was defeated!");
                }
            }
        }

        // Grant +10 attack per enemy defeated via a stacking status effect
        for (int i = 0; i < enemiesDefeated; i++) {
            performer.addStatusEffect(new ArcaneBlastEffect());
        }

        if (enemiesDefeated > 0) {
            System.out.println("Arcane Power absorbed! Attack increased by "
                    + (enemiesDefeated * 10) + ".");
        }
    }

    @Override
    public boolean isAvailable(Combatant performer) {
        return true;
    }
}