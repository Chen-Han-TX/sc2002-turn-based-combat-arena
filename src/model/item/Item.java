package model.item;

import model.combatant.Combatant;
import java.util.List;

/**
 * SHARED FILE — Do not edit without group agreement.
 * Interface for usable items (Potion, Power Stone, Smoke Bomb).
 */
public interface Item {

    /**
     * Get the display name of this item.
     */
    String getName();

    /**
     * Use this item during combat.
     * @param user       the combatant using the item
     * @param target     a specific target (can be null if item is self-use)
     * @param allEnemies all enemies currently in combat
     */
    void use(Combatant user, Combatant target, List<Combatant> allEnemies);

    /**
     * Check if this item has been consumed.
     */
    boolean isConsumed();
}
