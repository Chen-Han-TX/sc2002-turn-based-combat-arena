package model.combatant;

import model.action.Action;

//wolf enemy class
public class Wolf extends Combatant {

    public Wolf(String name) {
        super(name, 40, 45, 5, 35);
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
