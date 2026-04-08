package model.action;

import model.combatant.Warrior;
import model.effect.StatusEffect;
import java.util.ArrayList;

public class DefendDemo {
    public static void main(String[] args) {
        Warrior w = new Warrior();

        System.out.println("Before defend DEF = " + w.getDefense());

        Defend defend = new Defend();
        defend.execute(w, null, new ArrayList<>());

        System.out.println("After defend DEF = " + w.getDefense());

        for (StatusEffect effect : w.getStatusEffects()) {
            effect.tick();
            effect.tick();
        }

        w.removeExpiredEffects();

        System.out.println("After expiry DEF = " + w.getDefense());
    }
}