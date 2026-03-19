package model.combatant;

/**
 * Owner: Person A
 * Goblin enemy class.
 * HP: 55, Attack: 35, Defense: 15, Speed: 25
 */
public class Goblin extends Combatant {

    public Goblin(String name) {
        super(name, 55, 35, 15, 25);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
