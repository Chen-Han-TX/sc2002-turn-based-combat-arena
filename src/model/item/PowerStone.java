package model.item;

import model.combatant.Combatant;
import model.combatant.Wizard;
import java.util.List;

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

        /*
         * Assumption:
         * Person A / B should provide a method in Combatant such as:
         * user.useSpecialSkillWithoutCooldown(target, allEnemies);
         *
         * Replace the line below with the exact method name your team uses.
         */
        if (user instanceof Wizard) {
            ((Wizard) user).useArcaneBlast(allEnemies);
        }

        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}