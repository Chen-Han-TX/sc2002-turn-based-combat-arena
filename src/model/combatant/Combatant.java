package model.combatant;

import model.effect.StatusEffect;
import java.util.ArrayList;
import java.util.List;

/**
 * SHARED FILE — Do not edit without group agreement.
 * Base class for all characters in the game (players and enemies).
 */
public abstract class Combatant {
    protected String name;
    protected int maxHP;
    protected int currentHP;
    protected int attack;
    protected int defense;
    protected int speed;
    protected List<StatusEffect> statusEffects;
    protected boolean alive;

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHP = hp;
        this.currentHP = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.statusEffects = new ArrayList<>();
        this.alive = true;
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getMaxHP() { return maxHP; }
    public int getCurrentHP() { return currentHP; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    public boolean isAlive() { return alive; }
    public List<StatusEffect> getStatusEffects() { return statusEffects; }

    // --- HP management ---
    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - this.defense);
        this.currentHP = Math.max(0, this.currentHP - actualDamage);
        if (this.currentHP == 0) {
            this.alive = false;
        }
    }

    /**
     * Take raw damage that bypasses defense (e.g. already calculated).
     */
    public void takeRawDamage(int damage) {
        this.currentHP = Math.max(0, this.currentHP - damage);
        if (this.currentHP == 0) {
            this.alive = false;
        }
    }

    public void heal(int amount) {
        this.currentHP = Math.min(this.maxHP, this.currentHP + amount);
    }

    // --- Status effects ---
    public void addStatusEffect(StatusEffect effect) {
        this.statusEffects.add(effect);
    }

    public void removeExpiredEffects() {
        statusEffects.removeIf(StatusEffect::isExpired);
    }

    // --- Temporary stat modifiers (for buffs/debuffs) ---
    public void modifyDefense(int amount) { this.defense += amount; }
    public void modifyAttack(int amount) { this.attack += amount; }

    // --- Abstract: subclasses decide their behaviour ---
    public abstract boolean isPlayer();

    @Override
    public String toString() {
        return name + " [HP: " + currentHP + "/" + maxHP + "]";
    }
}
