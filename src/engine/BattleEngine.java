
package engine;

import model.action.Action;
import model.action.BasicAttack;
import model.action.Defend;
import model.combatant.Combatant;
import model.effect.SmokeBombEffect;
import model.effect.StatusEffect;
import model.effect.StunEffect;
import model.item.Item;
import ui.GameUI;

import java.util.ArrayList;
import java.util.List;

/**
 * BattleEngine
 *
 * Handles:
 * - round loop
 * - turn order
 * - player/enemy turns
 * - status effects
 * - special skill cooldown
 * - backup spawn
 * - victory / defeat
 *
 * Assumptions for this version:
 * - exactly 1 player
 * - enemies always use BasicAttack
 * - UI is CLI-based through GameUI
 * - items are already chosen before battle starts
 */
public class BattleEngine {
    private final Combatant player;
    private final List<Combatant> enemies;
    private final List<Combatant> backupSpawn;
    private final TurnOrderStrategy turnOrderStrategy;
    private final GameUI ui;
    private final List<Item> playerItems;
    private final boolean showEndScreens;

    private int roundNumber;
    private boolean battleOver;
    private boolean backupSpawnTriggered;

    /**
     * Cooldown for player special skill.
     * 0 = usable
     */
    private int playerSpecialCooldown;

    /**
     * Interactive constructor for actual gameplay.
     */
    public BattleEngine(Combatant player,
            List<Combatant> enemies,
            List<Combatant> backupSpawn,
            List<Item> playerItems,
            TurnOrderStrategy strategy,
            GameUI ui,
            boolean showEndScreens) {
		this.player = player;
		this.enemies = new ArrayList<>(enemies);
		this.backupSpawn = (backupSpawn != null) ? new ArrayList<>(backupSpawn) : new ArrayList<>();
		this.playerItems = (playerItems != null) ? new ArrayList<>(playerItems) : new ArrayList<>();
		this.turnOrderStrategy = strategy;
		this.ui = ui;
		this.showEndScreens = showEndScreens;
		
		this.roundNumber = 0;
		this.battleOver = false;
		this.backupSpawnTriggered = false;
		this.playerSpecialCooldown = 0;
		}

    public BattleEngine(Combatant player,
            List<Combatant> enemies,
            List<Combatant> backupSpawn,
            List<Item> playerItems,
            TurnOrderStrategy strategy,
            GameUI ui) {
		this(player, enemies, backupSpawn, playerItems, strategy, ui, true);
		}

    public void startBattle() {
        while (!battleOver) {
            roundNumber++;

            ui.showRoundStart(roundNumber, player, enemies);

            List<Combatant> turnOrder = buildTurnOrder();

            for (Combatant combatant : turnOrder) {
                if (battleOver) {
                    break;
                }

                if (!combatant.isAlive()) {
                    continue;
                }

                if (isPlayerDefeated()) {
                    battleOver = true;
                    break;
                }

                if (isPlayerVictory()) {
                    spawnBackupIfNeeded();

                    if (isPlayerVictory()) {
                        battleOver = true;
                        break;
                    }

                    // backup may have spawned, so stop this round and rebuild order next round
                    break;
                }

                processTurn(combatant);

                if (isPlayerDefeated()) {
                    battleOver = true;
                    break;
                }

                if (isPlayerVictory()) {
                    spawnBackupIfNeeded();

                    if (isPlayerVictory()) {
                        battleOver = true;
                        break;
                    }

                    // backup spawned -> next round will rebuild order
                    break;
                }
            }

            // End-of-round effect ticking
            tickAllEffects();
            removeExpiredEffects();
            ui.showRoundEnd(player, enemies);
        }

        if (showEndScreens) {
            showBattleResult();
        }
    }

    private List<Combatant> buildTurnOrder() {
        List<Combatant> turnOrder = new ArrayList<>();

        if (player.isAlive()) {
            turnOrder.add(player);
        }

        for (Combatant enemy : enemies) {
            if (enemy.isAlive()) {
                turnOrder.add(enemy);
            }
        }

        return turnOrderStrategy.determineTurnOrder(turnOrder);
    }

    private void processTurn(Combatant combatant) {
        // cooldown only decreases when the player's turn happens
        if (combatant == player && playerSpecialCooldown > 0) {
            playerSpecialCooldown--;
        }

        // apply start-of-turn effects
        applyEffectsAtStartOfTurn(combatant);

        // stunned?
        if (isUnableToAct(combatant)) {
            ui.showActionResult(combatant.getName() + " is stunned and skips the turn!");
            return;
        }

        combatant.passiveAbility();

        if (combatant.isPlayer()) {
            processPlayerTurn();
        } else {
            processEnemyTurn(combatant);
        }
    }

    private void applyEffectsAtStartOfTurn(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            effect.applyEffect(combatant);
        }
    }

    private boolean isUnableToAct(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            if (!effect.isExpired() && effect.preventsAction()) {
                return true;
            }
        }
        return false;
    }

    private void processPlayerTurn() {
        boolean actionCompleted = false;

        while (!actionCompleted) {
            List<String> actionNames = new ArrayList<>();
            actionNames.add("Basic Attack");
            actionNames.add("Defend");
            actionNames.add("Special Skill");
            actionNames.add("Use Item");

            int actionChoice = ui.promptActionChoice(actionNames, playerSpecialCooldown,
                    countNonConsumedItems());

            switch (actionChoice) {
                case 0:
                    performBasicAttack();
                    actionCompleted = true;
                    break;

                case 1:
                    new Defend().execute(player, null, enemies);
                    ui.showActionResult(player.getName() + " uses Defend!");
                    actionCompleted = true;
                    break;

                case 2:
                    if (playerSpecialCooldown > 0) {
                        ui.showActionResult("Special Skill is on cooldown for "
                                + playerSpecialCooldown + " more turn(s).");
                    } else {
                        performSpecialSkill(true);
                        actionCompleted = true;
                    }
                    break;

                case 3:
                    if (hasUsableItems()) {
                        performItemAction();
                        actionCompleted = true;
                    } else {
                        ui.showActionResult("No usable items remaining.");
                    }
                    break;

                default:
                    ui.showActionResult("Invalid action.");
            }
        }
    }

    private void performBasicAttack() {
        List<Combatant> aliveEnemies = getAliveEnemies();

        if (aliveEnemies.isEmpty()) {
            ui.showActionResult("No targets available.");
            return;
        }

        int targetIndex = ui.promptTargetChoice(aliveEnemies);
        Combatant target = aliveEnemies.get(targetIndex);

        new BasicAttack().execute(player, target, enemies);
    }

    /**
     * consumeCooldown = true for normal special skill usage
     * consumeCooldown = false for Power Stone usage
     */
    private void performSpecialSkill(boolean consumeCooldown) {
        Action specialSkill = player.getSpecialSkill();
        if (specialSkill == null) {
            ui.showActionResult("This player has no special skill implemented.");
            return;
        }

        // If the skill needs a target (e.g. Shield Bash), prompt for one
        if (specialSkill.needsTarget()) {
            List<Combatant> aliveEnemies = getAliveEnemies();
            if (aliveEnemies.isEmpty()) {
                ui.showActionResult("No targets available.");
                return;
            }
            int targetIndex = ui.promptTargetChoice(aliveEnemies);
            Combatant target = aliveEnemies.get(targetIndex);
            specialSkill.execute(player, target, enemies);
        } else {
            // AoE skills like Arcane Blast pass null target
            specialSkill.execute(player, null, enemies);
        }

        if (consumeCooldown) {
            playerSpecialCooldown = 3;
        }
    }



    private void performItemAction() {
        List<Item> usableItems = getUsableItems();

        if (usableItems.isEmpty()) {
            ui.showActionResult("No usable items remaining.");
            return;
        }

        List<String> itemNames = new ArrayList<>();
        for (Item item : usableItems) {
            itemNames.add(item.getName());
        }

        int itemChoice = ui.promptActionChoice(itemNames);
        Item chosenItem = usableItems.get(itemChoice);

        if ("Power Stone".equalsIgnoreCase(chosenItem.getName())) {
            Action specialSkill = player.getSpecialSkill();
            if (specialSkill != null && specialSkill.needsTarget()) {
                List<Combatant> aliveEnemies = getAliveEnemies();
                if (aliveEnemies.isEmpty()) {
                    ui.showActionResult("No targets available.");
                    return;
                }
                int targetIndex = ui.promptTargetChoice(aliveEnemies);
                Combatant target = aliveEnemies.get(targetIndex);
                chosenItem.use(player, target, enemies);
            } else {
                chosenItem.use(player, null, enemies);
            }
            ui.showActionResult(player.getName() + " uses Power Stone!");
            return;
        }

        chosenItem.use(player, null, enemies);
        ui.showActionResult(player.getName() + " uses " + chosenItem.getName() + "!");
    }

    private void processEnemyTurn(Combatant enemy) {
        if (!player.isAlive()) {
            return;
        }

        // Smoke Bomb: enemy attacks deal 0 damage
        if (hasActiveSmokeBomb(player)) {
            ui.showActionResult(enemy.getName() + " attacks, but Smoke Bomb reduces the damage to 0!");
            return;
        }

        new BasicAttack().execute(enemy, player, enemies);
    }

    private boolean hasActiveSmokeBomb(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            if (effect instanceof SmokeBombEffect && !effect.isExpired()) {
                return true;
            }
        }
        return false;
    }

    private void tickAllEffects() {
        tickEffectsForCombatant(player);

        for (Combatant enemy : enemies) {
            tickEffectsForCombatant(enemy);
        }
    }

    private void tickEffectsForCombatant(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            effect.tick();
        }
    }

    private void removeExpiredEffects() {
        player.removeExpiredEffects();

        for (Combatant enemy : enemies) {
            enemy.removeExpiredEffects();
        }
    }

    private void spawnBackupIfNeeded() {
        if (!backupSpawnTriggered && allCurrentEnemiesDefeated() && !backupSpawn.isEmpty()) {
            enemies.addAll(backupSpawn);
            backupSpawn.clear();
            backupSpawnTriggered = true;
            ui.showActionResult("All initial enemies defeated! Backup enemies appeared!");
        }
    }

    private boolean allCurrentEnemiesDefeated() {
        for (Combatant enemy : enemies) {
            if (enemy.isAlive()) {
                return false;
            }
        }
        return true;
    }

    private List<Combatant> getAliveEnemies() {
        List<Combatant> aliveEnemies = new ArrayList<>();
        for (Combatant enemy : enemies) {
            if (enemy.isAlive()) {
                aliveEnemies.add(enemy);
            }
        }
        return aliveEnemies;
    }

    private int countNonConsumedItems() {
        int count = 0;
        for (Item item : playerItems) {
            if (!item.isConsumed()) {
                count++;
            }
        }
        return count;
    }

    private boolean hasUsableItems() {
        for (Item item : playerItems) {
            if (!item.isConsumed()) {
                return true;
            }
        }
        return false;
    }

    private List<Item> getUsableItems() {
        List<Item> usable = new ArrayList<>();
        for (Item item : playerItems) {
            if (!item.isConsumed()) {
                usable.add(item);
            }
        }
        return usable;
    }

    private void showBattleResult() {
        if (isPlayerVictory()) {
            ui.showVictoryScreen(player.getCurrentHP(), player.getMaxHP(), roundNumber);
        } else if (isPlayerDefeated()) {
            ui.showDefeatScreen(countAliveEnemies(), roundNumber);
        }
    }

    private int countAliveEnemies() {
        int count = 0;
        for (Combatant enemy : enemies) {
            if (enemy.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Combatant getPlayer() {
        return player;
    }

    public List<Combatant> getEnemies() {
        return enemies;
    }

    public boolean isPlayerDefeated() {
        return !player.isAlive();
    }

    public boolean isPlayerVictory() {
        for (Combatant enemy : enemies) {
            if (enemy.isAlive()) {
                return false;
            }
        }
        return true;
    }
}
