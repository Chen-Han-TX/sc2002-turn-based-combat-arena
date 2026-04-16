package engine;

import model.combatant.Combatant;
import model.item.Item;
import ui.GameUI;

import java.util.List;

public class SurvivalGameRunner {
    private final Combatant player;
    private final List<Item> items;
    private final TurnOrderStrategy strategy;
    private final GameUI ui;
    private final GameMode mode;

    public SurvivalGameRunner(Combatant player,
                              List<Item> items,
                              TurnOrderStrategy strategy,
                              GameUI ui) {
        this.player = player;
        this.items = items;
        this.strategy = strategy;
        this.ui = ui;
        this.mode = new SurvivalMode();
    }

    public void start() {
        int waveNumber = 1;
        int totalEnemiesDefeated = 0;

        while (player.isAlive()) {
            ui.showActionResult("\n=== " + mode.getModeName() + " : Wave " + waveNumber + " ===");

            BattleEngine engine = new BattleEngine(
                    player,
                    mode.createWave(waveNumber),
                    null,
                    items,
                    strategy,
                    ui,
                    false
            );

            engine.startBattle();

            totalEnemiesDefeated += countDefeated(engine.getEnemies());

            if (!player.isAlive()) {
                ui.showActionResult("\n=== Survival Mode Defeat ===");
                ui.showActionResult("Waves Survived: " + (waveNumber - 1));
                ui.showActionResult("Total Enemies Defeated: " + totalEnemiesDefeated);
                ui.showActionResult("Final HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
                return;
            }

            ui.showActionResult("Wave " + waveNumber + " cleared!");
            player.heal(30);
            ui.showActionResult(player.getName() + " recovers 30 HP before the next wave.");

            waveNumber++;
        }
    }

    private int countDefeated(List<Combatant> enemies) {
        int count = 0;
        for (Combatant enemy : enemies) {
            if (!enemy.isAlive()) {
                count++;
            }
        }
        return count;
    }
}