package model.item;

import model.action.Action;
import model.combatant.Combatant;

import java.util.List;

/**
 * Power Stone:
 * Triggers the user's special skill once without changing cooldown.
 * Single use. Works for any player class via getSpecialSkill().
 */
public class PowerStone implements Item {
    private boolean consumed = false;

    @Override
    public String getName() {
        return "Power Stone";
    }

    @Override
    public void use(Combatant user, Combatant target, List<Combatant> allEnemies) {
        if (consumed) {
            return;
        }

        Action specialSkill = user.getSpecialSkill();
        if (specialSkill == null) {
            System.out.println("Power Stone failed: this combatant has no special skill.");
            return;
        }

        if (specialSkill.needsTarget()) {
            if (target == null || !target.isAlive()) {
                System.out.println("Power Stone failed: valid target required for "
                        + specialSkill.getName() + ".");
                return;
            }
            specialSkill.execute(user, target, allEnemies);
        } else {
            specialSkill.execute(user, null, allEnemies);
        }

        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}