package model.action;

import model.combatant.Combatant;
import java.util.List;

public class BasicAttack implements Action {

    @Override
    public String getName() {
        return "Basic Attack";
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {
        if (target == null || !target.isAlive()) {
            return;
        }

        int beforeHp = target.getCurrentHP();
        target.takeDamage(performer.getAttack());
        int actualDamage = beforeHp - target.getCurrentHP();

        System.out.println(performer.getName() + " attacks "
                + target.getName() + " for " + actualDamage + " damage.");
    }

    @Override
    public boolean isAvailable(Combatant performer) {
        return true;
    }
}