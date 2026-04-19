package model.item;

import java.util.List;
import model.combatant.Combatant;

public interface Item {

    String getName();

    void use(Combatant user, Combatant target, List<Combatant> allEnemies);

    boolean isConsumed();
}
