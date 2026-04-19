package model.action;

import java.util.List;
import model.combatant.Combatant;
import model.effect.ArcaneBlastEffect;

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