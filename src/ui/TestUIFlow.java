package ui;

import model.combatant.Combatant;
import model.combatant.Goblin;
import model.combatant.Warrior;
import model.combatant.Wolf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Person E test runner.
 *
 * Purpose:
 * - Verify UI-only responsibilities (display + input handling).
 * - Provide a clear red/green checklist while implementing GameUI/Main.
 *
 * How to run:
 *   cd src
 *   javac ui/TestUIFlow.java ui/GameUI.java Main.java
 *   java ui.TestUIFlow
 */
public class TestUIFlow {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName);
            failed++;
        }
    }

    private static String captureOutput(Runnable runnable) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(baos);
        try {
            System.setOut(capture);
            runnable.run();
        } finally {
            System.setOut(originalOut);
            capture.close();
        }
        return baos.toString();
    }

    private static GameUI createUIWithInput(String inputText) {
        System.setIn(new ByteArrayInputStream(inputText.getBytes()));
        return new GameUI();
    }

    private static boolean containsAny(String text, List<String> keywords) {
        String lower = text.toLowerCase();
        for (String keyword : keywords) {
            if (lower.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static void testLoadingScreen() {
        GameUI ui = createUIWithInput("\n");
        String output = captureOutput(ui::showLoadingScreen);

        check("Loading screen mentions Warrior/Wizard",
            containsAny(output, List.of("warrior", "wizard")));
        check("Loading screen mentions item choices",
            containsAny(output, List.of("potion", "power stone", "smoke bomb", "item")));
        check("Loading screen mentions difficulty choices",
            containsAny(output, List.of("easy", "medium", "hard", "difficulty")));
    }

    private static void testRoundStartDisplay() {
        GameUI ui = createUIWithInput("\n");
        Warrior player = new Warrior();
        List<Combatant> enemies = new ArrayList<>();
        enemies.add(new Goblin("Goblin A"));
        enemies.add(new Wolf("Wolf A"));

        String output = captureOutput(() -> ui.showRoundStart(3, player, enemies));

        check("Round start displays round number",
            containsAny(output, List.of("round 3", "round: 3", "3")));
        check("Round start displays player info",
            containsAny(output, List.of("warrior", "hp")));
        check("Round start displays enemy info",
            containsAny(output, List.of("goblin", "wolf")));
    }

    private static void testPromptActionChoice() {
        // Contract for this test: UI reads until valid input and returns zero-based index.
        GameUI ui = createUIWithInput("abc\n0\n3\n");
        List<String> actions = List.of("Basic Attack", "Defend", "Use Item");

        int chosen = ui.promptActionChoice(actions);
        check("Action choice validates input and returns index 2 for option 3", chosen == 2);
    }

    private static void testPromptTargetChoice() {
        // Contract for this test: UI reads until valid input and returns zero-based index.
        GameUI ui = createUIWithInput("-1\n2\n");
        List<Combatant> enemies = new ArrayList<>();
        enemies.add(new Goblin("Goblin A"));
        enemies.add(new Wolf("Wolf B"));

        int chosen = ui.promptTargetChoice(enemies);
        check("Target choice validates input and returns index 1 for option 2", chosen == 1);
    }

    private static void testActionResultDisplay() {
        GameUI ui = createUIWithInput("\n");
        String message = "Warrior uses Shield Bash on Goblin A";
        String output = captureOutput(() -> ui.showActionResult(message));

        check("Action result prints message", output.contains(message));
    }

    private static void testVictoryAndDefeatScreens() {
        GameUI ui = createUIWithInput("\n");

        String victoryOutput = captureOutput(() -> ui.showVictoryScreen(120, 260, 7));
        check("Victory screen includes success message",
            containsAny(victoryOutput, List.of("victory", "congrat", "defeated all")));
        check("Victory screen includes stats",
            containsAny(victoryOutput, List.of("120", "260", "7", "round")));

        String defeatOutput = captureOutput(() -> ui.showDefeatScreen(2, 5));
        check("Defeat screen includes defeat message",
            containsAny(defeatOutput, List.of("defeat", "try again", "lost")));
        check("Defeat screen includes stats",
            containsAny(defeatOutput, List.of("2", "5", "round", "enemies")));
    }

    private static void testMainEntryPointSmoke() {
        String output = captureOutput(() -> {
            try {
                Class<?> mainClass = Class.forName("Main");
                Method mainMethod = mainClass.getMethod("main", String[].class);
                mainMethod.invoke(null, (Object) new String[]{});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        check("Main entry point runs without crash", !output.isBlank());
        check("Main output shows game title",
            containsAny(output, List.of("turn-based combat arena", "combat arena")));
    }

    public static void main(String[] args) {
        System.out.println("=== Person E UI Tests ===\n");

        testLoadingScreen();
        testRoundStartDisplay();
        testPromptActionChoice();
        testPromptTargetChoice();
        testActionResultDisplay();
        testVictoryAndDefeatScreens();
        testMainEntryPointSmoke();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
        if (failed > 0) {
            System.out.println("Some tests are expected to fail until GameUI/Main are fully implemented.");
        }
    }
}
