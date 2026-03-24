package model.effect;

import model.combatant.Combatant;

/**
 * Owner: Person C
 * Stun: Target cannot act for current turn and next turn.
 */
public class StunEffect implements StatusEffect {
    private int turnsRemaining;

    public StunEffect(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
    }

    @Override
    public String getName() {
        return "Stun";
    }

    @Override
    public void applyEffect(Combatant target) {
        // No immediate stat change needed
    }

    @Override
    public void tick() {
        if (turnsRemaining > 0) {
            turnsRemaining--;
        }
    }

    @Override
    public boolean preventsAction() {
        return true;
    }

    @Override
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }

    @Override
    public void onExpire(Combatant target) {
        // No cleanup needed
    }
}