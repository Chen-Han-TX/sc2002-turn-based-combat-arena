package model.action;

import model.combatant.Combatant;
import java.util.List;

/**
 * Owner: Person B
 * Basic attack action: Damage = max(0, Attacker Attack - Target Defense)
 */
public class BasicAttack implements Action {

    @Override
    public String getName() {
        return "Basic Attack";
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {
        // TODO: implement damage calculation
        // damage = max(0, performer.getAttack() - target.getDefense())
        // target.takeRawDamage(damage)  <-- use raw since we calc ourselves
    }

    @Override
    public boolean isAvailable(Combatant performer) {
        return true; // always available
    }
}
