package engine;

import model.combatant.Combatant;
import java.util.ArrayList;
import java.util.List;

/**
 * Owner: Person D
 * Sorts combatants by speed (highest first).
 */
public class SpeedBasedOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        // TODO: sort by speed descending
        // higher speed goes first
        List<Combatant> sorted = new ArrayList<>(combatants);
        sorted.sort((a, b) -> b.getSpeed() - a.getSpeed());
        return sorted;
    }
}
