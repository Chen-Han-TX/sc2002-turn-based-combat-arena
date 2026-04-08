package model.combatant;

import model.effect.StatusEffect;
import model.effect.ArcaneBlastEffect;
//import model.effect.ArcaneBlastEffect; // Needed for the attack check
import java.util.ArrayList;
import java.util.List;

/**
 * SHARED FILE — Updated to support Status Effect stat bonuses.
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

    // --- Getters (Updated for Status Effects) ---
    public String getName() { return name; }
    public int getMaxHP() { return maxHP; }
    public int getCurrentHP() { return currentHP; }
    
 
//      Calculates total attack including bonuses from Status Effects.

    public int getAttack() { 
        int totalAttack = this.attack;
        for (StatusEffect effect : statusEffects) {
            if (effect instanceof ArcaneBlastEffect) {
                totalAttack += 10;
            }
        }
        return totalAttack; 
    }

    /**
     * Calculates total defense including potential Status Effects.
     */
    public int getDefense() { 
        int totalDefense = this.defense;
        // Logic for 'Defend' status effect can be added here similarly
        return totalDefense; 
    }

    public int getSpeed() { return speed; }
    public boolean isAlive() { return currentHP > 0; }
    public List<StatusEffect> getStatusEffects() { return statusEffects; }

    // --- HP management ---
    public void takeDamage(int damage) {
        // Uses the dynamic getDefense() in case of buffs
        int actualDamage = Math.max(0, damage - this.getDefense());
        this.currentHP = Math.max(0, this.currentHP - actualDamage);
        if (this.currentHP == 0) {
            this.alive = false;
        }
    }

    public void takeRawDamage(int damage) {
        this.currentHP = Math.max(0, this.currentHP - damage);
        if (this.currentHP == 0) {
            this.alive = false;
        }
    }

    public void heal(int amount) {
        if (isAlive()) {
            this.currentHP = Math.min(this.maxHP, this.currentHP + amount);
        }
    }

    // --- Status effects ---
    public void addStatusEffect(StatusEffect effect) {
        this.statusEffects.add(effect);
    }

    public void removeExpiredEffects() {
        statusEffects.removeIf(StatusEffect::isExpired);
    }

    // --- Temporary stat modifiers ---
    public void modifyDefense(int amount) { this.defense += amount; }
    public void modifyAttack(int amount) { this.attack += amount; }

    // --- Abstract: subclasses decide their behaviour ---
    public abstract boolean isPlayer();
    /*
    @Override
    public String toString() {
        return name + " [HP: " + currentHP + "/" + maxHP + " | ATK: " + getAttack() + "]";
    }*/
}