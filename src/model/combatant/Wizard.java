package model.combatant;

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
        // Base stats from assignment document
        super("Wizard", 200, 50, 10, 20);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }


//     * Special Skill: Arcane Blast
//     * Effect: Deal BasicAttack damage to all enemies currently in combat.
//     * Each enemy defeated by Arcane Blast adds 10 to the Wizard’s Attack,
//     * lasting until end of the level.
//     * * @param enemies The list of enemies in the current battle.
     
    public void useArcaneBlast(List<Combatant> enemies) {
        int enemiesDefeated = 0;

        System.out.println(this.getName() + " casts Arcane Blast!");

        // 1. Loop through all enemies
        for (Combatant enemy : enemies) {
            // Only hit living enemies
            if (enemy.isAlive()) {
                // Calculate damage (BasicAttack)
                int currentAtk = this.getAttack();
                enemy.takeDamage(currentAtk);
                
                System.out.println("Blast hit " + enemy.getName() + " for " + currentAtk + " damage.");

                // 2. Check if the enemy died
                if (!enemy.isAlive()) {
                    enemiesDefeated++;
                    System.out.println(enemy.getName() + " was defeated!");
                }
            }
        }

        // 3. Apply Arcane Blast Status Effect for each kill
        // This follows the "Status Effects" section of your document
        for (int i = 0; i < enemiesDefeated; i++) {
            this.addStatusEffect(new ArcaneBlastEffect());
        }

        if (enemiesDefeated > 0) {
            System.out.println("Arcane Power absorbed! Attack increased by " + (enemiesDefeated * 10) + ".");
        }
    }


//     Overriding getAttack to include bonuses from Status Effects 
//     like Arcane Blast.
     
    @Override
    public int getAttack() {
        int bonus = 0;
        for (var effect : statusEffects) {
            if (effect instanceof ArcaneBlastEffect) {
                bonus += 10;
            }
        }
        return this.attack + bonus;
   }
}