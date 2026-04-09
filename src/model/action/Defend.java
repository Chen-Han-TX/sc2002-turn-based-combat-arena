package model.action;

import model.combatant.Combatant;
import model.effect.DefendBuff;
import model.effect.StatusEffect;
import java.util.List;

public class Defend implements Action {

    @Override
    public String getName() {
        return "Defend";
    }

    @Override
    public boolean needsTarget() {
        return false;
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {

        int duration = 2;

        StatusEffect defenseBuff = new DefendBuff(duration);

        performer.addStatusEffect(defenseBuff);

        defenseBuff.applyEffect(performer);

        System.out.println(performer.getName() + " takes a defensive stance!");
    }

    @Override
    public boolean isAvailable(Combatant performer) {
        return true;
    }
}
