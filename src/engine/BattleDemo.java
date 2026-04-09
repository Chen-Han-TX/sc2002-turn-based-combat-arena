package engine;

import model.combatant.*;
import java.util.ArrayList;
import java.util.List;

public class BattleDemo {
    public static void main(String[] args) {

        Combatant player = new Warrior();  // try Wizard later too

        List<Combatant> enemies = new ArrayList<>();
        enemies.add(new Goblin("Goblin A"));
        enemies.add(new Wolf("Wolf A"));

        BattleEngine engine = new BattleEngine(
                player,
                enemies,
                null,
                new SpeedBasedOrder()
        );

        engine.startBattle();

        System.out.println("\n=== Battle Finished ===");
        System.out.println("Rounds: " + engine.getRoundNumber());
        System.out.println("Player alive: " + player.isAlive());
    }
}