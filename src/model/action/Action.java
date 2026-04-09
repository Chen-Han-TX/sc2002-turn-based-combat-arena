package model.action;

import model.combatant.Combatant;
import java.util.List;

/**
 * SHARED FILE — Do not edit without group agreement.
 * Interface for all combat actions (BasicAttack, Defend, SpecialSkill, UseItem).
 */
public interface Action {

    /**
     * Get the display name of this action for the CLI menu.
     */
    String getName();

    /**
     * Execute this action.
     * @param performer  the combatant performing the action
     * @param target     the target combatant (can be null for self-targeting or AoE)
     * @param allEnemies all enemies currently in combat (for AoE skills like Arcane Blast)
     */
    void execute(Combatant performer, Combatant target, List<Combatant> allEnemies);

    /**
     * Check if this action can be used right now.
     * e.g. special skill might be on cooldown, items might be consumed.
     */
    boolean isAvailable(Combatant performer);

    /**
     * Whether this action requires a specific target.
     * Default true. Override to false for AoE or self-targeting actions.
     */
    default boolean needsTarget() {
        return true;
    }
}
