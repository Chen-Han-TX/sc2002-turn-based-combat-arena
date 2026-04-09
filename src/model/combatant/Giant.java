package model.combatant;

import model.action.Action;
import model.action.DoubleSmash;

/**
 * Giant player class.
 * HP: 500, Attack: 35, Defense: 30, Speed: 10
 * Special Skill: Double Smash — attacks the same target twice in one turn.
 */
public class Giant extends Combatant {

    public Giant() {
        super("Giant", 500, 35, 30, 10);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public Action getSpecialSkill() {
        return new DoubleSmash();
    }
}
