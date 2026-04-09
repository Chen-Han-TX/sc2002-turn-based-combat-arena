package ui;

import model.combatant.Combatant;
import model.effect.StatusEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Person E stuff
 */
public final class BattleDisplay {
    private BattleDisplay() {}

    public static String combatantSummary(Combatant combatant) {
        String state = combatant.isAlive() ? "Alive" : "Eliminated";
        return combatant.getName()
            + " [HP: " + combatant.getCurrentHP() + "/" + combatant.getMaxHP()
            + ", Effects: " + effectNames(combatant.getStatusEffects())
            + ", " + state + "]";
    }

    private static String effectNames(List<StatusEffect> effects) {
        if (effects == null || effects.isEmpty()) {
            return "None";
        }

        List<String> names = new ArrayList<>();
        for (StatusEffect effect : effects) {
            names.add(effect.getName());
        }
        return String.join(", ", names);
    }
}
