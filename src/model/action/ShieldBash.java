package model.action;

import model.combatant.Combatant;
import model.effect.StunEffect;

import java.util.List;

/**
 * Warrior special skill:
 * - deals normal attack damage to one target
 * - stuns target for current turn and next turn
 */
public class ShieldBash implements Action {

    @Override
    public String getName() {
        return "Shield Bash";
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {
        if (target == null || !target.isAlive()) {
            return;
        }

        int beforeHp = target.getCurrentHP();
        target.takeDamage(performer.getAttack());
        int actualDamage = beforeHp - target.getCurrentHP();

        if (target.isAlive()) {
            target.addStatusEffect(new StunEffect(2));
            System.out.println(performer.getName() + " uses Shield Bash on "
                    + target.getName() + " for " + actualDamage
                    + " damage and stuns the target!");
        } else {
            System.out.println(performer.getName() + " uses Shield Bash on "
                    + target.getName() + " for " + actualDamage
                    + " damage and defeats the target!");
        }
    }

    @Override
    public boolean isAvailable(Combatant performer) {
        return true;
    }
}