package model.effect;

import model.combatant.Combatant;

public interface StatusEffect {

    String getName();

    //Apply this effect at the start of a turn (before the combatant acts)
    void applyEffect(Combatant target);

    //Called at the end of each round to reduce remaining duration
    void tick();

    boolean isExpired();

    //Remove/undo the effect when it expires 
    void onExpire(Combatant target);

    boolean preventsAction();
}
