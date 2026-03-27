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
               int damage = Math.max(0, performer.getAttack() - target.getDefense());
        target.takeRawDamage(damage);
        System.out.println(performer.getName() + " deals " + damage + " damage to " + target.getName());
    }
    
    @Override
    public boolean isAvailable(Combatant performer) {
        return true; // always available
    }
}
