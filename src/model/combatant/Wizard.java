package model.combatant;

import model.action.Action;
import model.action.ArcaneBlast;
import model.effect.ArcaneBlastEffect;
import java.util.List;

/**
 * Owner: Person A
 * Wizard player class.
 * HP: 200, Attack: 50, Defense: 10, Speed: 20
 * Special Skill: Arcane Blast
 */
public class Wizard extends Combatant {

    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public Action getSpecialSkill() {
        return new ArcaneBlast();
    }

    @Override 
    public void passiveAbility() {
        modifyAttack(1);{
        System.out.println(getName() + "'s passive activated. Attack +1.");
        }
    }

    /**
     * Special Skill: Arcane Blast
     * Hits all living enemies.
     * Each enemy defeated grants +10 attack (via ArcaneBlastEffect).
     */
    public void useArcaneBlast(List<Combatant> enemies) {
        int enemiesDefeated = 0;

        System.out.println(this.getName() + " casts Arcane Blast!");

        for (Combatant enemy : enemies) {
            if (enemy != null && enemy.isAlive()) {

                int beforeHp = enemy.getCurrentHP();
                enemy.takeDamage(this.getAttack());
                int actualDamage = beforeHp - enemy.getCurrentHP();

                System.out.println("Blast hit " + enemy.getName() 
                        + " for " + actualDamage + " damage.");

                if (!enemy.isAlive()) {
                    enemiesDefeated++;
                    System.out.println(enemy.getName() + " was defeated!");
                }
            }
        }

        // Add +10 attack per enemy defeated
        for (int i = 0; i < enemiesDefeated; i++) {
            this.addStatusEffect(new ArcaneBlastEffect());
        }

        if (enemiesDefeated > 0) {
            System.out.println("Arcane Power absorbed! Attack increased by " 
                    + (enemiesDefeated * 10) + ".");
        }
    }
}
