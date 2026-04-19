package model.action;

import java.util.List;
import model.combatant.Combatant;

//Interface for all combat actions (BasicAttack, Defend, SpecialSkill, UseItem)
public interface Action {

    String getName();

    void execute(Combatant performer, Combatant target, List<Combatant> allEnemies);

    boolean isAvailable(Combatant performer);

    default boolean needsTarget() {
        return true;
    }
}
