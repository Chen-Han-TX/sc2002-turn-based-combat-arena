package model.effect;

import model.combatant.Combatant;


public class SmokeBombEffect implements StatusEffect {
    private int turnsRemaining;

    public SmokeBombEffect(int turnsRemaining) {
        this.turnsRemaining = turnsRemaining;
    }

    @Override
    public String getName() {
        return "Smoke Bomb Effect";
    }

    @Override
    public void applyEffect(Combatant target) {
        // No direct stat change here
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
        // No cleanup needed
    }

}