# SC2002 Turn-Based Combat Arena

A CLI turn-based combat game built in Java for **SC2002 Object-Oriented Design & Programming** (AY25/26 Sem 2, NTU). Demonstrates SOLID principles and a clean Boundary–Control–Entity architecture.

**Lab Group:** FDAD · **Group:** 1
**Team:** Anthony Lin Zihan · Cha Heng Ping · Chen Han · Chew Qi Siang · Chloe Seah Hsueh Ern
**Repo:** https://github.com/Chen-Han-TX/sc2002-turn-based-combat-arena

---

## How to Run

**Requires:** Java 17+ (JDK).

```bash
cd src
javac Main.java
java Main --interactive
```

---

## Gameplay

1. Pick **game mode** → Classic or Survival
2. Pick **player** → Warrior, Wizard, or Giant *(additional)*
3. Pick **2 items** → Potion / Power Stone / Smoke Bomb (duplicates allowed)
4. Pick **difficulty** (Classic only) → Easy / Medium / Hard

Each round, combatants act in descending speed order. The player wins by eliminating all enemies; loses at 0 HP. After an initial wave is defeated, backup enemies (if any) enter simultaneously.

### Players

| Player | HP | ATK | DEF | SPD | Special Skill | Passive *(additional)* |
|--------|----|-----|-----|-----|---------------|------------------------|
| **Warrior** | 260 | 40 | 20 | 30 | **Shield Bash** — damage + stun target for 2 turns | +1 DEF/round |
| **Wizard**  | 200 | 50 | 10 | 20 | **Arcane Blast** — AoE; +10 ATK (until end of level) per kill | +1 ATK/round |
| **Giant** *(additional)* | 400 | 35 | 20 | 10 | **Double Smash** — attack same target twice | +3 HP heal/round |

All special skills have a **3-turn cooldown**, decrementing only on turns the combatant actually takes.

### Enemies

| Enemy | HP | ATK | DEF | SPD |
|-------|----|-----|-----|-----|
| Goblin | 55 | 35 | 15 | 25 |
| Wolf   | 40 | 45 |  5 | 35 |

Enemies always perform BasicAttack. Selection is routed through `Combatant.chooseAction()` so new AI strategies can be added without modifying `BattleEngine`.

### Actions, Items, Effects

| Action | Effect |
|--------|--------|
| BasicAttack | `max(0, ATK − targetDEF)` damage |
| Defend | +10 DEF for current + next round |
| SpecialSkill | Class-specific; 3-turn cooldown |
| Item | Consume one of your 2 chosen items |

| Item | Effect |
|------|--------|
| Potion | Heal 100 HP (capped at Max) |
| Power Stone | Free special skill use, no cooldown change |
| Smoke Bomb | Enemy attacks deal 0 damage for 2 turns |

| Status Effect | Duration |
|---------------|----------|
| Stun | 2 turns (prevents action) |
| DefendBuff | 2 turns (+10 DEF) |
| SmokeBombEffect | 2 turns (nullifies incoming damage) |
| ArcaneBlastEffect | Until end of level (+10 ATK per stack) |

### Difficulty

| Level | Initial Wave | Backup Wave |
|-------|--------------|-------------|
| Easy   | 3 Goblins          | — |
| Medium | 1 Goblin + 1 Wolf  | 2 Wolves |
| Hard   | 2 Goblins          | 1 Goblin + 2 Wolves |

### Survival Mode *(additional)*

Endless wave-based mode driven by the `GameMode` interface and `SurvivalGameRunner`. Waves scale in composition; HP and skill buffs persist across waves; player heals +30 HP after each cleared wave. Run ends at 0 HP with stats for waves survived and total enemies defeated. Added without modifying `BattleEngine`.

---

## Architecture

```
src/
├── Main.java                      Entry + setup prompts
│
├── engine/                        CONTROL (BCE)
│   ├── BattleEngine               Round loop, turns, win/lose
│   ├── TurnOrderStrategy          Interface → SpeedBasedOrder
│   ├── GameMode                   Interface → SurvivalMode
│   └── SurvivalGameRunner         Drives repeated battles
│
├── model/                         ENTITY (BCE)
│   ├── combatant/                 Combatant (abstract) + Warrior/Wizard/Giant/Goblin/Wolf
│   ├── action/                    Action interface + BasicAttack/Defend/ShieldBash/ArcaneBlast/DoubleSmash
│   ├── item/                      Item interface + Potion/PowerStone/SmokeBomb
│   └── effect/                    StatusEffect interface + Stun/DefendBuff/SmokeBomb/ArcaneBlast effects
│
└── ui/                            BOUNDARY (BCE)
    ├── GameUI                     All screens and prompts
    ├── InputHandler               Validated numeric input
    └── BattleDisplay              Combatant formatting
```

The UI layer contains **no game rules**. The engine depends on abstractions — never on concrete classes.

---

## SOLID Evidence

| Principle | Applied |
|-----------|---------|
| **SRP** | Each class has one job: `BasicAttack` does one attack, `GameUI` only renders/prompts, `BattleEngine` only runs rounds, `SurvivalGameRunner` only drives waves. |
| **OCP** | New actions, items, effects, combatants, and game modes added by implementing interfaces — zero changes to `BattleEngine`. Giant and Survival Mode were added without touching engine code. |
| **LSP** | All combatants substitute for `Combatant` everywhere; engine never does `instanceof` on player classes. |
| **ISP** | Five focused interfaces (`Action`, `Item`, `StatusEffect`, `TurnOrderStrategy`, `GameMode`) rather than one monolith. Each exposes only what its clients need. |
| **DIP** | `BattleEngine` depends on interfaces only. Turn ordering is injected (`TurnOrderStrategy`) — swapping to random ordering needs no engine changes. |

**Patterns:** Strategy (`TurnOrderStrategy`, `GameMode`), Command (`Action`), Template Method (`Combatant.passiveAbility()`), Composition (`Combatant` ◆→ `StatusEffect*`).

---

## Team Responsibilities

| Person | Package |
|--------|---------|
| Anthony Lin Zihan | `model/combatant/` |
| Cha Heng Ping | `model/action/` |
| Chen Han | `model/item/` + `model/effect/` |
| Chew Qi Siang | `engine/` |
| Chloe Seah Hsueh Ern | `ui/` + `Main.java` |

Shared interfaces (`Combatant`, `Action`, `Item`, `StatusEffect`, `TurnOrderStrategy`, `GameMode`) require group agreement before modification.