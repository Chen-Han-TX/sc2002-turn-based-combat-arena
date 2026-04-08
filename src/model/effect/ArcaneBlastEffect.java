package model.effect;

import model.combatant.Combatant;

/**
 * Arcane Blast Effect:
 * Each instance represents +10 attack for the Wizard.
 * Lasts until the end of the level, so it does not expire by turns.
 *
 * Note:
 * The Wizard class handles the actual bonus by counting how many
 * ArcaneBlastEffect objects are in its statusEffects list.
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
        // Do nothing here.
        // Wizard.getAttack() already handles the bonus by checking
        // how many ArcaneBlastEffect objects are active.
    }

    @Override
    public void tick() {
        // Do nothing.
        // This effect lasts until the end of the level.
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
        // Do nothing here.
        // Since attack is not directly modified in applyEffect(),
        // there is nothing to undo.
    }
}