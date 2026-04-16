package model.combatant;

import model.action.Action;
import model.action.ShieldBash;
/**
 * Owner: Person A
 * Warrior player class.
 * HP: 260, Attack: 40, Defense: 20, Speed: 30
 * Special Skill: Shield Bash
 */
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

