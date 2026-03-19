package model.effect;

import model.combatant.Combatant;

/**
 * SHARED FILE — Do not edit without group agreement.
 * Interface for effects that persist across turns (Stun, Defend buff, Smoke Bomb, etc.)
 */
public interface StatusEffect {

    /**
     * Get the display name of this effect.
     */
    String getName();

    /**
     * Apply this effect at the start of a turn (before the combatant acts).
     * e.g. Stun prevents action, Smoke Bomb blocks damage.
     * @param target the combatant this effect is on
     */
    void applyEffect(Combatant target);

    /**
     * Called at the end of each round to reduce remaining duration.
     */
    void tick();

    /**
     * Check if this effect has expired.
     */
    boolean isExpired();

    /**
     * Remove/undo the effect when it expires (e.g. remove bonus defense).
     * @param target the combatant this effect was on
     */
    void onExpire(Combatant target);

    /**
     * Does this effect prevent the combatant from acting?
     */
    boolean preventsAction();
}
