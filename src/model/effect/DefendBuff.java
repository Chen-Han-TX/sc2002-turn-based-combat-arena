package model.effect;

import model.combatant.Combatant;

/**
 * Defend Buff:
 * Increases defense temporarily for 2 turns.
 */
public class DefendBuff implements StatusEffect {
    private int turnsRemaining;
    private boolean applied;

    public DefendBuff(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
        this.applied = false;
    }

    @Override
    public String getName() {
        return "Defend Buff";
    }

    @Override
    public void applyEffect(Combatant target) {
        if (!applied) {
            target.modifyDefense(10);
            applied = true;
        }
    }

    @Override
    public void tick() {
        if (turnsRemaining > 0) {
            turnsRemaining--;
        }
    }

    @Override
    public boolean preventsAction() {
        return false;
    }

    @Override
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }

    @Override
    public void onExpire(Combatant target) {
        if (applied) {
            target.modifyDefense(-10);
            applied = false;
        }
    }
}