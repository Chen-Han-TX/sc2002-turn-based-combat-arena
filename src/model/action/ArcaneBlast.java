package model.action;

import model.combatant.Combatant;
import model.combatant.Wizard;

import java.util.List;

/**
 * Wizard special skill action wrapper.
 * Uses Wizard's existing useArcaneBlast logic.
 */
public class ArcaneBlast implements Action {

    @Override
    public String getName() {
        return "Arcane Blast";
    }

    @Override
    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {
        if (!(performer instanceof Wizard)) {
            System.out.println("Only Wizard can use Arcane Blast.");
            return;
        }

        ((Wizard) performer).useArcaneBlast(allEnemies);
    }

    @Override
    public boolean isAvailable(Combatant performer) {
        return performer instanceof Wizard;
    }
}