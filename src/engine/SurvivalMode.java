package engine;

import model.combatant.Combatant;
import model.combatant.Goblin;
import model.combatant.Wolf;

import java.util.ArrayList;
import java.util.List;

public class SurvivalMode implements GameMode {

    @Override
    public String getModeName() {
        return "Survival Mode";
    }

    @Override
    public List<Combatant> createWave(int waveNumber) {
        List<Combatant> enemies = new ArrayList<>();

        if (waveNumber == 1) {
            enemies.add(new Goblin("Goblin A"));
            enemies.add(new Goblin("Goblin B"));
        } else if (waveNumber == 2) {
            enemies.add(new Goblin("Goblin A"));
            enemies.add(new Wolf("Wolf A"));
        } else if (waveNumber == 3) {
            enemies.add(new Wolf("Wolf A"));
            enemies.add(new Wolf("Wolf B"));
        } else {
            int goblinCount = Math.max(1, waveNumber - 2);
            int wolfCount = Math.max(1, waveNumber - 2);

            for (int i = 0; i < goblinCount; i++) {
                enemies.add(new Goblin("Goblin " + (char) ('A' + i)));
            }

            for (int i = 0; i < wolfCount; i++) {
                enemies.add(new Wolf("Wolf " + (char) ('A' + i)));
            }
        }

        return enemies;
    }
}