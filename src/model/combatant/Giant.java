package model.combatant;

import model.action.Action;
import model.action.DoubleSmash;

/**
 * Giant player class.
 * HP: 400, Attack: 35, Defense: 20, Speed: 10
 * Special Skill: Double Smash — attacks the same target twice in one turn.
 */
public class Giant extends Combatant {

    public Giant() {
        super("Giant", 400, 35, 20, 10);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public Action getSpecialSkill() {
        return new DoubleSmash();
    }
    @Override 
    public void passiveAbility() {
        if (getCurrentHP() < getMaxHP()) {
            heal(3);
            System.out.println(getName() + "'s passive activated. Defence +1.");            
        }
    }
}
