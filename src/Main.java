/**
 * Entry point for the Turn-Based Combat Arena game.
 * Owner: Person E
 */
import ui.GameUI;

public class Main {
    public static void main(String[] args) {
        GameUI ui = new GameUI();
        ui.showLoadingScreen();
        System.out.println("\nUI ready. Waiting for engine integration.");
    }
}
