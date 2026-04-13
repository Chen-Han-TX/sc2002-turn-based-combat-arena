package model.combatant;

import model.action.Action;
import model.effect.StatusEffect;
import model.effect.ArcaneBlastEffect;
import java.util.ArrayList;
import java.util.List;

/**
 * SHARED FILE
 * Base class for all characters (player + enemies).
 * Supports status effects (buffs/debuffs).
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

    // --- Basic Getters ---
    public String getName() { return name; }
    public int getMaxHP() { return maxHP; }
    public int getCurrentHP() { return currentHP; }
    public int getSpeed() { return speed; }
    public boolean isAlive() { return currentHP > 0; }
    public List<StatusEffect> getStatusEffects() { return statusEffects; }

    // --- Attack (includes Arcane Blast bonuses) ---
    public int getAttack() {
        int totalAttack = this.attack;

        for (StatusEffect effect : statusEffects) {
            if (effect instanceof ArcaneBlastEffect) {
                totalAttack += 10;
            }
        }

        return totalAttack;
    }

    // --- Defense ---
    public int getDefense() {
        return this.defense;
    }

    // --- HP management ---
    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - getDefense());
        currentHP = Math.max(0, currentHP - actualDamage);

        if (currentHP == 0) {
            alive = false;
        }
    }

    public void takeRawDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);

        if (currentHP == 0) {
            alive = false;
        }
    }

    public void heal(int amount) {
        if (isAlive()) {
            currentHP = Math.min(maxHP, currentHP + amount);
        }
    }

    // --- Status Effects ---
    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
    }

    /**
     * VERY IMPORTANT:
     * Removes expired effects AND correctly removes their stat bonuses.
     */
    public void removeExpiredEffects() {
        List<StatusEffect> toRemove = new ArrayList<>();

        for (StatusEffect effect : statusEffects) {
            if (effect.isExpired()) {
                effect.onExpire(this);   // remove buff/debuff
                toRemove.add(effect);
            }
        }

        statusEffects.removeAll(toRemove);
    }

    // --- Temporary stat modifiers ---
    public void modifyDefense(int amount) {
        this.defense += amount;
    }

    public void modifyAttack(int amount) {
        this.attack += amount;
    }

    public void passiveAbility(){
        
    }

    // --- Abstract ---
    public abstract boolean isPlayer();

    /**
     * Returns the special skill Action for this combatant, or null if none.
     */
    public abstract Action getSpecialSkill();
}
