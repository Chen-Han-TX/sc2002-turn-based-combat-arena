package model.item;

import java.util.List;
import model.combatant.Combatant;

//potion: heals 100
public class Potion implements Item {
    private boolean consumed = false;

    @Override
    public String getName() {
        return "Potion";
    }

    @Override
    public void use(Combatant user, Combatant target, List<Combatant> allEnemies) {
        if (consumed) {
            return;
        }

        user.heal(100);
        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}