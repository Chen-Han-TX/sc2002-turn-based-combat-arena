import model.combatant.Combatant;
import model.combatant.Goblin;
import model.combatant.Warrior;
import model.combatant.Wizard;
import model.combatant.Wolf;
import model.action.ArcaneBlast;
import model.action.BasicAttack;
import model.action.Defend;
import model.action.ShieldBash;
import model.effect.ArcaneBlastEffect;
import model.effect.SmokeBombEffect;
import model.effect.StatusEffect;
import model.effect.StunEffect;
import model.item.Potion;
import model.item.PowerStone;
import model.item.SmokeBomb;

import java.util.ArrayList;
import java.util.List;

public class TestGameFlow {

    static int passed = 0;
    static int failed = 0;

    static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName);
            failed++;
        }
    }
    static boolean hasActiveSmokeBomb(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            if (effect instanceof SmokeBombEffect && !effect.isExpired()) {
                return true;
            }
        }
        return false;
    }

    static void enemyBasicUnlessSmoke(Combatant player, Combatant enemy, List<Combatant> ctx) {
        if (hasActiveSmokeBomb(player)) {
            return;
        }
        new BasicAttack().execute(enemy, player, ctx);
    }

    static boolean isStunned(Combatant c) {
        for (StatusEffect e : c.getStatusEffects()) {
            if (e instanceof StunEffect && !e.isExpired() && e.preventsAction()) {
                return true;
            }
        }
        return false;
    }

    static long countArcaneBlastEffects(Combatant c) {
        return c.getStatusEffects().stream().filter(ArcaneBlastEffect.class::isInstance).count();
    }

    static List<Combatant> mainInitialEnemies(int difficulty) {
        List<Combatant> enemies = new ArrayList<>();
        switch (difficulty) {
            case 0:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                enemies.add(new Goblin("Goblin C"));
                break;
            case 1:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Wolf("Wolf A"));
                break;
            case 2:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                break;
            default:
                enemies.add(new Goblin("Goblin A"));
                enemies.add(new Goblin("Goblin B"));
                enemies.add(new Goblin("Goblin C"));
        }
        return enemies;
    }

    static List<Combatant> mainBackupEnemies(int difficulty) {
        List<Combatant> backup = new ArrayList<>();
        switch (difficulty) {
            case 0:
                break;
            case 1:
                backup.add(new Wolf("Wolf B"));
                backup.add(new Wolf("Wolf C"));
                break;
            case 2:
                backup.add(new Goblin("Goblin C"));
                backup.add(new Wolf("Wolf A"));
                backup.add(new Wolf("Wolf B"));
                break;
            default:
                break;
        }
        return backup;
    }

    static int countInstances(List<Combatant> list, Class<?> type) {
        int n = 0;
        for (Combatant c : list) {
            if (type.isInstance(c)) {
                n++;
            }
        }
        return n;
    }

    static void testCombatantStatsAndLevelCompositions() {
        System.out.println("\n=== §3.1 / §3.5 — Stats & level compositions ===\n");

        Warrior w = new Warrior();
        check("Warrior HP/ATK/DEF/SPD", w.getMaxHP() == 260 && w.getAttack() == 40
                && w.getDefense() == 20 && w.getSpeed() == 30);

        Wizard wiz = new Wizard();
        check("Wizard HP/ATK/DEF/SPD", wiz.getMaxHP() == 200 && wiz.getAttack() == 50
                && wiz.getDefense() == 10 && wiz.getSpeed() == 20);

        Goblin g = new Goblin("G");
        check("Goblin HP/ATK/DEF/SPD", g.getMaxHP() == 55 && g.getAttack() == 35
                && g.getDefense() == 15 && g.getSpeed() == 25);

        Wolf wolf = new Wolf("W");
        check("Wolf HP/ATK/DEF/SPD", wolf.getMaxHP() == 40 && wolf.getAttack() == 45
                && wolf.getDefense() == 5 && wolf.getSpeed() == 35);

        List<Combatant> easyI = mainInitialEnemies(0);
        List<Combatant> easyB = mainBackupEnemies(0);
        check("Easy: initial 3 Goblins, 0 Wolves", countInstances(easyI, Goblin.class) == 3
                && countInstances(easyI, Wolf.class) == 0);
        check("Easy: no backup spawn", easyB.isEmpty());

        List<Combatant> medI = mainInitialEnemies(1);
        List<Combatant> medB = mainBackupEnemies(1);
        check("Medium: initial 1 Goblin + 1 Wolf",
                countInstances(medI, Goblin.class) == 1 && countInstances(medI, Wolf.class) == 1);
        check("Medium: backup 2 Wolves", countInstances(medB, Wolf.class) == 2 && medB.size() == 2);

        List<Combatant> hardI = mainInitialEnemies(2);
        List<Combatant> hardB = mainBackupEnemies(2);
        check("Hard: initial 2 Goblins", countInstances(hardI, Goblin.class) == 2);
        check("Hard: backup 1 Goblin + 2 Wolves",
                countInstances(hardB, Goblin.class) == 1 && countInstances(hardB, Wolf.class) == 2);
    }

    static void testDefendReducesGoblinDamage() {
        System.out.println("\n=== §6.2 UML hint — Defend vs Goblin BasicAttack ===\n");

        Warrior warrior = new Warrior();
        Goblin goblin = new Goblin("Goblin");
        List<Combatant> ctx = new ArrayList<>(List.of(goblin));

        new Defend().execute(warrior, null, ctx);
        int hpBefore = warrior.getCurrentHP();
        new BasicAttack().execute(goblin, warrior, ctx);
        int dealt = hpBefore - warrior.getCurrentHP();
        check("After Defend, Goblin damage is 5 (35−30)", dealt == 5);
    }

    // --- Appendix A (i) Easy ---
    static void testAppendixA_Easy() {
        System.out.println("\n=== Appendix A (i) — Easy (Warrior, 3 Goblins) ===\n");

        Warrior warrior = new Warrior();
        Goblin gA = new Goblin("Goblin A");
        Goblin gB = new Goblin("Goblin B");
        Goblin gC = new Goblin("Goblin C");
        BasicAttack attack = new BasicAttack();
        ShieldBash shieldBash = new ShieldBash();
        List<Combatant> enemies = new ArrayList<>(List.of(gA, gB, gC));

        System.out.println("--- Round 1 (PDF) ---");
        attack.execute(warrior, gA, enemies);
        check("R1 Warrior → Goblin A: HP 55→30", gA.getCurrentHP() == 30);
        attack.execute(gA, warrior, enemies);
        check("R1 Goblin A → Warrior: HP 260→245", warrior.getCurrentHP() == 245);
        attack.execute(gB, warrior, enemies);
        check("R1 Goblin B → Warrior: HP 245→230", warrior.getCurrentHP() == 230);
        attack.execute(gC, warrior, enemies);
        check("R1 Goblin C → Warrior: HP 230→215", warrior.getCurrentHP() == 215);

        System.out.println("\n--- Round 2 — Shield Bash + stun (PDF) ---");
        shieldBash.execute(warrior, gA, enemies);
        check("R2 Shield Bash → Goblin A: HP 30→5", gA.getCurrentHP() == 5);
        check("R2 Goblin A stunned", isStunned(gA));

        System.out.println("\n--- Round 3 — eliminate Goblin A (PDF) ---");
        attack.execute(warrior, gA, enemies);
        check("R3 BasicAttack → Goblin A eliminated", gA.getCurrentHP() == 0 && !gA.isAlive());

        System.out.println("\n--- Round 4 — Smoke Bomb: enemy hits 0 damage (engine rule) ---");
        Warrior smokeWarrior = new Warrior();
        smokeWarrior.takeRawDamage(105);
        check("R4 setup: Warrior HP 155 (PDF)", smokeWarrior.getCurrentHP() == 155);
        List<Combatant> smokeCtx = new ArrayList<>(List.of(gB, gC));
        new SmokeBomb().use(smokeWarrior, null, smokeCtx);
        enemyBasicUnlessSmoke(smokeWarrior, gB, smokeCtx);
        enemyBasicUnlessSmoke(smokeWarrior, gC, smokeCtx);
        check("R4 Smoke Bomb: Warrior HP stays 155", smokeWarrior.getCurrentHP() == 155);

        System.out.println("\n--- Round 7 — Potion +100 (PDF, isolated) ---");
        Warrior potionWarrior = new Warrior();
        potionWarrior.takeRawDamage(120);
        check("R7 setup: Warrior HP 140", potionWarrior.getCurrentHP() == 140);
        new Potion().use(potionWarrior, null, enemies);
        check("Potion: HP 140→240 (min with max HP)", potionWarrior.getCurrentHP() == 240);
    }

    // --- Appendix A (ii) Medium Warrior ---
    static void testAppendixA_MediumWarrior() {
        System.out.println("\n=== Appendix A (ii) — Medium (Warrior, Goblin + Wolf) ===\n");

        Warrior warrior = new Warrior();
        Goblin goblin = new Goblin("Goblin");
        Wolf wolf = new Wolf("Wolf");
        BasicAttack attack = new BasicAttack();
        ShieldBash shieldBash = new ShieldBash();
        List<Combatant> party = new ArrayList<>(List.of(goblin, wolf));

        System.out.println("--- Rounds 1–2 (PDF) ---");
        attack.execute(wolf, warrior, party);
        check("R1 Wolf → Warrior: HP 260→235", warrior.getCurrentHP() == 235);
        shieldBash.execute(warrior, wolf, party);
        check("R1 Shield Bash → Wolf: HP 40→5", wolf.getCurrentHP() == 5);
        check("R1 Wolf stunned", isStunned(wolf));
        attack.execute(goblin, warrior, party);
        check("R1 Goblin → Warrior: HP 235→220", warrior.getCurrentHP() == 220);

        attack.execute(warrior, wolf, party);
        check("R2 Warrior → Wolf eliminated", !wolf.isAlive());
        attack.execute(goblin, warrior, party);
        check("R2 Goblin → Warrior: HP 220→205", warrior.getCurrentHP() == 205);

        System.out.println("\n--- Rounds 3–5 up to Goblin elimination (PDF) ---");
        attack.execute(warrior, goblin, party);
        check("R3 Warrior → Goblin: HP 55→30", goblin.getCurrentHP() == 30);
        attack.execute(goblin, warrior, party);
        check("R3 Goblin → Warrior: HP 205→190", warrior.getCurrentHP() == 190);

        attack.execute(warrior, goblin, party);
        check("R4 Warrior → Goblin: HP 30→5", goblin.getCurrentHP() == 5);
        attack.execute(goblin, warrior, party);
        check("R4 Goblin → Warrior: HP 190→175", warrior.getCurrentHP() == 175);

        shieldBash.execute(warrior, goblin, party);
        check("R5 Shield Bash → Goblin eliminated", !goblin.isAlive());

        System.out.println("\n--- Round 6 — Power Stone → Shield Bash, cooldown unchanged (skill effect only) ---");
        Wolf wolfA = new Wolf("Wolf A");
        Wolf wolfB = new Wolf("Wolf B");
        List<Combatant> backup = new ArrayList<>(List.of(wolfA, wolfB));
        PowerStone stone = new PowerStone();
        stone.use(warrior, wolfA, backup);
        check("Power Stone consumed", stone.isConsumed());
        check("R6 Power Stone Shield Bash → Wolf A: HP 40→5", wolfA.getCurrentHP() == 5);
        check("R6 Wolf A stunned", isStunned(wolfA));
    }

    // --- Appendix A (iii) Medium Wizard ---
    static void testAppendixA_MediumWizard() {
        System.out.println("\n=== Appendix A (iii) — Medium (Wizard, Arcane Blast + Power Stone) ===\n");

        Wizard wizard = new Wizard();
        Goblin goblin = new Goblin("Goblin");
        Wolf wolf = new Wolf("Wolf");
        BasicAttack attack = new BasicAttack();
        ArcaneBlast arcane = new ArcaneBlast();
        List<Combatant> initial = new ArrayList<>(List.of(wolf, goblin));

        System.out.println("--- Round 1 (PDF) ---");
        attack.execute(wolf, wizard, initial);
        check("R1 Wolf → Wizard: HP 200→165", wizard.getCurrentHP() == 165);
        attack.execute(goblin, wizard, initial);
        check("R1 Goblin → Wizard: HP 165→140", wizard.getCurrentHP() == 140);

        arcane.execute(wizard, null, initial);
        check("R1 Arcane: Wolf eliminated", !wolf.isAlive());
        check("R1 Arcane: Goblin HP 55→20", goblin.getCurrentHP() == 20);
        check("R1 Wizard ATK 50→60 (one Arcane kill)", wizard.getAttack() == 60);
        check("R1 one ArcaneBlastEffect stored", countArcaneBlastEffects(wizard) == 1);

        System.out.println("\n--- Round 2 (PDF) ---");
        attack.execute(goblin, wizard, initial);
        check("R2 Goblin → Wizard: HP 140→115", wizard.getCurrentHP() == 115);
        attack.execute(wizard, goblin, initial);
        check("R2 Wizard Basic → Goblin eliminated (60−15)", !goblin.isAlive());

        System.out.println("\n--- Round 3 — Power Stone Arcane on two Wolves (PDF) ---");
        Wolf wolfA = new Wolf("Wolf A");
        Wolf wolfB = new Wolf("Wolf B");
        List<Combatant> backup = new ArrayList<>(List.of(wolfA, wolfB));
        PowerStone stone = new PowerStone();
        stone.use(wizard, null, backup);
        check("R3 both backup wolves eliminated", !wolfA.isAlive() && !wolfB.isAlive());
        check("R3 Wizard final ATK 80", wizard.getAttack() == 80);
        check("R3 three ArcaneBlastEffect total", countArcaneBlastEffects(wizard) == 3);
        check("Wizard HP unchanged during R3 test (115)", wizard.getCurrentHP() == 115);
    }

    public static void main(String[] args) {
        passed = 0;
        failed = 0;

        System.out.println("=== SC2002 TestGameFlow — PDF Appendix A & related checkpoints ===\n");

        testCombatantStatsAndLevelCompositions();
        testDefendReducesGoblinDamage();
        testAppendixA_Easy();
        testAppendixA_MediumWarrior();
        testAppendixA_MediumWizard();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
        if (failed > 0) {
            System.out.println("\nCompare failing cases with \"Appendix A: Game Flow Examples\" in the assignment PDF.");
            System.exit(1);
        }
        System.out.println("\nAll TestGameFlow checks passed.");
    }
}
