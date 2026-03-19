package model.combatant;

/**
 * Owner: Person A
 * Wolf enemy class.
 * HP: 40, Attack: 45, Defense: 5, Speed: 35
 */
public class Wolf extends Combatant {

    public Wolf(String name) {
        super(name, 40, 45, 5, 35);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
