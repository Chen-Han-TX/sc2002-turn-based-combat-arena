package engine;

import java.util.List;
import model.combatant.Combatant;

public interface TurnOrderStrategy {

    /**
     * @param combatants all alive combatants in the current round
     * @return sorted list, first element acts first
     */
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}
