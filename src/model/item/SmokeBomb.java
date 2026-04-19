package model.item;

import java.util.List;
import model.combatant.Combatant;
import model.effect.SmokeBombEffect;

/**
 * Smoke Bomb: Enemy attacks deal 0 damage for current turn and next turn, single use
 */
public class SmokeBomb implements Item {
    private boolean consumed = false;

    @Override
    public String getName() {
        return "Smoke Bomb";
    }

    @Override
    public void use(Combatant user, Combatant target, List<Combatant> allEnemies) {
        if (consumed) {
            return;
        }

        user.addStatusEffect(new SmokeBombEffect(2));
        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}