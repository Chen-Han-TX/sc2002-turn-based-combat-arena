package engine;

import model.combatant.Combatant;
import java.util.List;

public interface GameMode {
    String getModeName();
    List<Combatant> createWave(int waveNumber);
}