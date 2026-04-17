package engine;

import model.combatant.Combatant;
import java.util.ArrayList;
import java.util.List;

public class SpeedBasedOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {

        List<Combatant> sorted = new ArrayList<>(combatants);
        sorted.sort((a, b) -> b.getSpeed() - a.getSpeed());
        return sorted;
    }
}
