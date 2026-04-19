package model.combatant;

import model.action.Action;
import model.action.ShieldBash;

//warrior player class
public class Warrior extends Combatant {

    public Warrior() {
        super("Warrior", 260, 40, 20, 30);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public Action getSpecialSkill() {
        return new ShieldBash();
    }

    @Override
    public void passiveAbility() {
        modifyDefense(1);
        System.out.println(getName() + "'s passive activated. Defense +1.");
    }
        

}

