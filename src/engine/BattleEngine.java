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
 public class BattleEngine {
    private final Player player;
    private final Level level;
    private final SpawnManager spawnManager;
    private final TurnOrderStrategy turnOrderStrategy;
    private final GameUI gameUI;

    private final List<Enemy> activeEnemies = new ArrayList<>();
    private final List<Enemy> allEnemiesSeen = new ArrayList<>();

    private int roundNumber = 0;
    private boolean backupSpawned = false;

    public BattleEngine(Player player,
                        Level level,
                        SpawnManager spawnManager,
                        TurnOrderStrategy turnOrderStrategy,
                        GameUI gameUI) {
        this.player = player;
        this.level = level;
        this.spawnManager = spawnManager;
        this.turnOrderStrategy = turnOrderStrategy;
        this.gameUI = gameUI;
    }

    public BattleResult startBattle() {
        spawnInitialWave();
        gameUI.showBattleStart(player, level, getAliveEnemies());

        while (!isBattleOver()) {
            roundNumber++;
            gameUI.showRoundStart(roundNumber, player, getAliveEnemies());
            runRound();
            spawnBackupWaveIfNeeded();
        }

        BattleResult result = buildResult();
        resetEndOfLevelState();
        return result;
    }

    private void runRound() {
        List<Combatant> turnOrder = buildTurnOrder();
        gameUI.showTurnOrder(turnOrder);

        for (Combatant combatant : turnOrder) {
            if (isBattleOver()) {
                break;
            }

            if (!combatant.isAlive()) {
                continue;
            }

            // Apply effects that happen at start of turn
            combatant.onTurnStart();
            removeDefeatedEnemies();

            if (isBattleOver()) {
                break;
            }

            // Combatant may die from poison/burn/etc. at turn start
            if (!combatant.isAlive()) {
                continue;
            }

            // Stun / freeze / sleep / etc.
            if (combatant.isUnableToAct()) {
                gameUI.showSkippedTurn(combatant);
                combatant.onTurnEnd();
                removeDefeatedEnemies();
                gameUI.showTurnSummary(player, getAliveEnemies());
                continue;
            }

            if (combatant.isPlayer()) {
                executePlayerTurn((Player) combatant);
            } else {
                executeEnemyTurn((Enemy) combatant);
            }

            removeDefeatedEnemies();

            // Apply end-of-turn cleanup/effects
            combatant.onTurnEnd();
            removeDefeatedEnemies();

            gameUI.showTurnSummary(player, getAliveEnemies());
        }
    }

    private void executePlayerTurn(Player actingPlayer) {
        Action action = gameUI.chooseAction(actingPlayer, getAliveEnemies());
        if (action == null) {
            throw new IllegalStateException("Player action cannot be null");
        }
        action.execute(actingPlayer, this);
    }

    private void executeEnemyTurn(Enemy enemy) {
        Action action = enemy.decideAction(player, getAliveEnemies(), this);
        if (action == null) {
            throw new IllegalStateException("Enemy action cannot be null");
        }
        action.execute(enemy, this);
    }

    private List<Combatant> buildTurnOrder() {
        List<Combatant> combatants = new ArrayList<>();
        if (player.isAlive()) {
            combatants.add(player);
        }
        combatants.addAll(getAliveEnemies());
        return turnOrderStrategy.determineTurnOrder(combatants);
    }

    private void spawnInitialWave() {
        List<Enemy> initialWave = spawnManager.spawnInitialWave(level);
        activeEnemies.addAll(initialWave);
        allEnemiesSeen.addAll(initialWave);
        removeDefeatedEnemies();
    }

    private void spawnBackupWaveIfNeeded() {
        if (backupSpawned || !level.hasBackupWave() || hasLivingEnemies()) {
            return;
        }

        List<Enemy> backupWave = spawnManager.spawnBackupWave(level);
        if (backupWave == null || backupWave.isEmpty()) {
            throw new IllegalStateException(
                "Level indicates a backup wave exists, but SpawnManager returned no backup enemies."
            );
        }

        backupSpawned = true;
        activeEnemies.addAll(backupWave);
        allEnemiesSeen.addAll(backupWave);
        removeDefeatedEnemies();
        gameUI.showBackupSpawn(new ArrayList<>(backupWave));
    }

    private void removeDefeatedEnemies() {
        activeEnemies.removeIf(enemy -> !enemy.isAlive());
    }

    private boolean hasLivingEnemies() {
        return !getAliveEnemies().isEmpty();
    }

    public boolean isBattleOver() {
        return !player.isAlive()
                || (!hasLivingEnemies() && (backupSpawned || !level.hasBackupWave()));
    }

    public boolean areAllEnemiesDefeated() {
        return !hasLivingEnemies() && (backupSpawned || !level.hasBackupWave());
    }

    private BattleResult buildResult() {
        boolean playerWon = player.isAlive() && areAllEnemiesDefeated();
        int enemiesRemaining = (int) allEnemiesSeen.stream()
                .filter(Enemy::isAlive)
                .count();

        return new BattleResult(
                playerWon,
                roundNumber,
                player.getCurrentHp(),
                enemiesRemaining,
                level.getDifficultyName()
        );
    }

    private void resetEndOfLevelState() {
        player.onLevelEnd();
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public List<Enemy> getAliveEnemies() {
        List<Enemy> aliveEnemies = new ArrayList<>();
        for (Enemy enemy : activeEnemies) {
            if (enemy.isAlive()) {
                aliveEnemies.add(enemy);
            }
        }
        return aliveEnemies;
    }

    public List<Enemy> getAllEnemiesSeen() {
        return new ArrayList<>(allEnemiesSeen);
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    // end-of-battle reporting.

    public static class BattleResult {
        private final boolean playerWon;
        private final int totalRounds;
        private final int remainingHp;
        private final int enemiesRemaining;
        private final String difficultyName;

        public BattleResult(boolean playerWon,
                            int totalRounds,
                            int remainingHp,
                            int enemiesRemaining,
                            String difficultyName) {
            this.playerWon = playerWon;
            this.totalRounds = totalRounds;
            this.remainingHp = remainingHp;
            this.enemiesRemaining = enemiesRemaining;
            this.difficultyName = difficultyName;
        }

        public boolean isPlayerWon() {
            return playerWon;
        }

        public int getTotalRounds() {
            return totalRounds;
        }

        public int getRemainingHp() {
            return remainingHp;
        }

        public int getEnemiesRemaining() {
            return enemiesRemaining;
        }

        public String getDifficultyName() {
            return difficultyName;
        }
    }
}
