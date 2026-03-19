package model.combatant;

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

    // TODO: Add any Warrior-specific methods if needed
}
