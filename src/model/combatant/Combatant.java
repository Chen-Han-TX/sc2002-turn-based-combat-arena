package model.combatant;

import java.util.ArrayList;
import java.util.List;
import model.action.Action;
import model.effect.ArcaneBlastEffect;
import model.effect.StatusEffect;


public abstract class Combatant {
    protected String name;
    protected int maxHP;
    protected int currentHP;
    protected int attack;
    protected int defense;
    protected int speed;
    protected List<StatusEffect> statusEffects;

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHP = hp;
        this.currentHP = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.statusEffects = new ArrayList<>();
    }

    public String getName() { return name; }
    public int getMaxHP() { return maxHP; }
    public int getCurrentHP() { return currentHP; }
    public int getSpeed() { return speed; }
    public boolean isAlive() { return currentHP > 0; }
    public List<StatusEffect> getStatusEffects() { return statusEffects; }

    public int getAttack() {
        int totalAttack = this.attack;

        for (StatusEffect effect : statusEffects) {
            if (effect instanceof ArcaneBlastEffect) {
                totalAttack += 10;
            }
        }

        return totalAttack;
    }

    public int getDefense() {
        return this.defense;
    }

    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - getDefense());
        currentHP = Math.max(0, currentHP - actualDamage);
    }

    public void takeRawDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);
    }

    public void heal(int amount) {
        if (isAlive()) {
            currentHP = Math.min(maxHP, currentHP + amount);
        }
    }

    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
    }

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

    public void modifyDefense(int amount) {
        this.defense += amount;
    }

    public void modifyAttack(int amount) {
        this.attack += amount;
    }

    public void passiveAbility(){
        
    }

    public abstract boolean isPlayer();


    public abstract Action getSpecialSkill();
}
