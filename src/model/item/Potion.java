package model.item;

import model.combatant.Combatant;
import java.util.List;

/**
 * Owner: Person C
 * Potion: Heals 100 HP. Single use. New HP = min(currentHP + 100, maxHP)
 */
public class Potion implements Item {
    private boolean consumed = false;

    @Override
    public String getName() {
        return "Potion";
    }

    @Override
    public void use(Combatant user, Combatant target, List<Combatant> allEnemies) {
        // TODO: heal user by 100, cap at maxHP
        // user.heal(100);
        // consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}
