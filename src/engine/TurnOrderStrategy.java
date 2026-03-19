package engine;

import model.combatant.Combatant;
import java.util.List;

/**
 * SHARED FILE — Do not edit without group agreement.
 * Strategy interface for determining turn order each round.
 * Supports future extensibility (e.g. random order, initiative-based).
 */
public interface TurnOrderStrategy {

    /**
     * Given a list of alive combatants, return them sorted by turn order.
     * @param combatants all alive combatants in the current round
     * @return sorted list (first element acts first)
     */
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}
