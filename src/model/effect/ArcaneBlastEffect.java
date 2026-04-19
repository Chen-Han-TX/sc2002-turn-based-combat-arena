package model.effect;

import model.combatant.Combatant;

/**
 * Arcane Blast Effect:
 * Each instance represents +10 attack for the wizard
 * Lasts until the end of the level, so it does not expire by turns.
 */
public class ArcaneBlastEffect implements StatusEffect {
    private int attackBonus;

    public ArcaneBlastEffect() {
        this.attackBonus = 10;
    }

 
    public ArcaneBlastEffect(int attackBonus) {
        this.attackBonus = attackBonus;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public String getName() {
        return "Arcane Blast Effect";
    }

    @Override
    public void applyEffect(Combatant target) {
    }

    @Override
    public void tick() {
    }

    @Override
    public boolean preventsAction() {
        return false;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void onExpire(Combatant target) {
    }
}