package model.action;

import model.combatant.Combatant;
import java.util.List;

/**
 * Giant special skill: Double Smash.
 * Performs two consecutive BasicAttack hits on the same target in a single turn.
 * Cooldown follows the standard 3-turn rule managed by BattleEngine.
 */
public class DoubleSmash implements Action {

    @Override
    public String getName() {
        return "Double Smash";
    }

    @Override
    public boolean needsTarget() {
        return true;
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {
        if (target == null || !target.isAlive()) {
            return;
        }

        System.out.println(performer.getName() + " winds up for a Double Smash!");

        // First hit
        int beforeFirst = target.getCurrentHP();
        target.takeDamage(performer.getAttack());
        int firstDamage = beforeFirst - target.getCurrentHP();
        System.out.println("Hit 1: " + performer.getName() + " smashes "
                + target.getName() + " for " + firstDamage + " damage.");

        // Second hit — only if target is still alive
        if (target.isAlive()) {
            int beforeSecond = target.getCurrentHP();
            target.takeDamage(performer.getAttack());
            int secondDamage = beforeSecond - target.getCurrentHP();
            System.out.println("Hit 2: " + performer.getName() + " smashes "
                    + target.getName() + " for " + secondDamage + " damage.");
        } else {
            System.out.println(target.getName() + " was eliminated after the first hit!");
        }
    }

    @Override
    public boolean isAvailable(Combatant performer) {
        return true;
    }
}
