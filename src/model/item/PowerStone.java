package model.item;

import model.combatant.Combatant;
import java.util.List;

/**
 * Owner: Person C
 * Power Stone: Instantly triggers the user's special skill once,
 * without changing cooldown. Single use.
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

        /*
         * Assumption:
         * Person A / B should provide a method in Combatant such as:
         * user.useSpecialSkillWithoutCooldown(target, allEnemies);
         *
         * Replace the line below with the exact method name your team uses.
         */
        user.useSpecialSkillWithoutCooldown(target, allEnemies);

        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}