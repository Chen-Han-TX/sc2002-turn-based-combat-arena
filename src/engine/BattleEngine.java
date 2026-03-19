package engine;

import model.combatant.Combatant;
import model.action.Action;
import model.effect.StatusEffect;
import java.util.List;
import java.util.ArrayList;

/**
 * Owner: Person D
 * Core battle loop: manages rounds, turns, win/lose conditions.
 * Depends on abstractions only (Action, Combatant, StatusEffect, TurnOrderStrategy).
 */
public class BattleEngine {
    private Combatant player;
    private List<Combatant> enemies;
    private List<Combatant> backupSpawn;
    private TurnOrderStrategy turnOrderStrategy;
    private int roundNumber;
    private boolean battleOver;

    public BattleEngine(Combatant player, List<Combatant> enemies,
                        List<Combatant> backupSpawn, TurnOrderStrategy strategy) {
        this.player = player;
        this.enemies = new ArrayList<>(enemies);
        this.backupSpawn = backupSpawn != null ? new ArrayList<>(backupSpawn) : new ArrayList<>();
        this.turnOrderStrategy = strategy;
        this.roundNumber = 0;
        this.battleOver = false;
    }

    /**
     * TODO: Implement the main battle loop.
     * Each round:
     *   1. Determine turn order
     *   2. For each combatant in order:
     *      a. Apply status effects (check if stunned)
     *      b. Choose/execute action
     *      c. Check if game over
     *   3. Tick status effects, remove expired ones
     *   4. Check for backup spawn trigger
     */
    public void startBattle() {
        // TODO: Person D implements this
    }

    public boolean isPlayerVictory() {
        return enemies.stream().noneMatch(Combatant::isAlive);
    }

    public boolean isPlayerDefeated() {
        return !player.isAlive();
    }

    public int getRoundNumber() { return roundNumber; }
    public Combatant getPlayer() { return player; }
    public List<Combatant> getEnemies() { return enemies; }
}
