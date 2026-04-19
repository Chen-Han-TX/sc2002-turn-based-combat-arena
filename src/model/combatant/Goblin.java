package model.combatant;

import model.action.Action;

//goblin enemy class
public class Goblin extends Combatant {

    public Goblin(String name) {
        super(name, 55, 35, 15, 25);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public Action getSpecialSkill() {
        return null;
    }
}
