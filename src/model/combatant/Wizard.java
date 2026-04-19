package model.combatant;

import model.action.Action;
import model.action.ArcaneBlast;

//wizard player class
public class Wizard extends Combatant {

    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public Action getSpecialSkill() {
        return new ArcaneBlast();
    }

    @Override 
    public void passiveAbility() {
        modifyAttack(1);{
        System.out.println(getName() + "'s passive activated. Attack +1.");
        }
    }

}
