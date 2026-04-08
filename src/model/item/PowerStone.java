package model.item;

import model.action.ArcaneBlast;
import model.action.ShieldBash;
import model.combatant.Combatant;
import model.combatant.Warrior;
import model.combatant.Wizard;

import java.util.List;

/**
 * Power Stone:
 * Triggers special skill once without changing cooldown.
 * Single use.
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

        if (user instanceof Wizard) {
            new ArcaneBlast().execute(user, null, allEnemies);
        } else if (user instanceof Warrior) {
            if (target == null || !target.isAlive()) {
                System.out.println("Power Stone failed: valid target required for Shield Bash.");
                return;
            }
            new ShieldBash().execute(user, target, allEnemies);
        } else {
            System.out.println("This combatant has no special skill.");
            return;
        }

        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}