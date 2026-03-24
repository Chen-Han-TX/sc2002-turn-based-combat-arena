package model.effect;

import model.combatant.Combatant;

public class DefendBuff implements StatusEffect {
    private int turnsRemaining;

    public DefendBuff(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
    }

    @Override
    public String getName() {
        return "Defend Buff";
    }

    @Override
    public void applyEffect(Combatant target) {
        // Leave empty if defense bonus is handled elsewhere
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
        // Leave empty
    }
}