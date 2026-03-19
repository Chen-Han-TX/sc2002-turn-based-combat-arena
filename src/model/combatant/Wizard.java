package model.combatant;

/**
 * Owner: Person A
 * Wizard player class.
 * HP: 200, Attack: 50, Defense: 10, Speed: 20
 * Special Skill: Arcane Blast
 */
public class Wizard extends Combatant {

    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    // TODO: Add any Wizard-specific methods if needed
}
