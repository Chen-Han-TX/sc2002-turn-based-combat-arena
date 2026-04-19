package model.combatant;

import model.action.Action;
import model.action.DoubleSmash;

//Giant player class
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
            System.out.println(getName() + "'s passive activated. Healed 3 HP.");
        }
    }
}
