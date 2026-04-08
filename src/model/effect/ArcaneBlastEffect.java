package model.effect;

import model.combatant.Combatant;

/**
 * Arcane Blast Effect:
 * Increases attack by a fixed amount.
 * Lasts until the end of the level, so it does not expire by turns.
 */
public class ArcaneBlastEffect implements StatusEffect {
    private int attackBonus;
    private boolean applied;

    public ArcaneBlastEffect(int attackBonus) {
        this.attackBonus = attackBonus;
        this.applied = false;
    }

    @Override
    public String getName() {
        return "Arcane Blast Effect";
    }

    @Override
    public void applyEffect(Combatant target) {
        if (!applied) {
            target.modifyAttack(attackBonus);
            applied = true;
        }
    }

    @Override
    public void tick() {
        // Do nothing.
        // This buff lasts until the end of the level, not by turn count.
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
        if (applied) {
            target.modifyAttack(-attackBonus);
            applied = false;
        }
    }
}