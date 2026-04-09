# SC2002 Turn-Based Combat Arena

A CLI turn-based combat game built in Java for **SC2002 Object-Oriented Design & Programming** (AY25/26 Sem 2, NTU). The project demonstrates SOLID design principles, UML modeling, and clean layered architecture through an extensible combat system.

---

## How to Run

**Requires:** Java 17+

```bash
cd src
javac Main.java
java Main --interactive
```

Running without `--interactive` prints the title and exits (smoke test mode).

## Game Overview

A player (Warrior or Wizard) battles waves of enemies (Goblins and Wolves) across three difficulty levels. Each round, combatants act in speed order. The player wins by eliminating all enemies; the player loses if their HP reaches zero.

### Setup Phase

1. Choose a player class (Warrior or Wizard)
2. Pick 2 items from: Potion, Power Stone, Smoke Bomb (duplicates allowed)
3. Select difficulty: Easy, Medium, or Hard

### Combat Actions

| Action | Description |
|--------|-------------|
| **Basic Attack** | `damage = max(0, ATK - target DEF)` on one target |
| **Defend** | +10 DEF for current round and next round |
| **Special Skill** | Warrior: Shield Bash (single-target damage + 2-turn stun). Wizard: Arcane Blast (hits all enemies, +10 ATK per kill for rest of level). 3-turn cooldown. |
| **Use Item** | Potion (heal 100 HP), Power Stone (free special skill, no cooldown change), Smoke Bomb (enemies deal 0 damage for 2 turns) |

### Difficulty Levels

| Level | Initial Enemies | Backup Spawn |
|-------|----------------|--------------|
| Easy | 3 Goblins | None |
| Medium | 1 Goblin + 1 Wolf | 2 Wolves |
| Hard | 2 Goblins | 1 Goblin + 2 Wolves |

Backup enemies spawn after the initial wave is fully defeated.

---

## Project Architecture

```
src/
├── Main.java                          <- Entry point + game loop
├── model/
│   ├── combatant/
│   │   ├── Combatant.java             <- Abstract base (shared contract)
│   │   ├── Warrior.java               <- Player class (Shield Bash)
│   │   ├── Wizard.java                <- Player class (Arcane Blast)
│   │   ├── Goblin.java                <- Enemy (HP:55 ATK:35 DEF:15 SPD:25)
│   │   └── Wolf.java                  <- Enemy (HP:40 ATK:45 DEF:5 SPD:35)
│   ├── action/
│   │   ├── Action.java                <- Interface (shared contract)
│   │   ├── BasicAttack.java
│   │   ├── Defend.java
│   │   ├── ShieldBash.java
│   │   └── ArcaneBlast.java
│   ├── item/
│   │   ├── Item.java                  <- Interface (shared contract)
│   │   ├── Potion.java
│   │   ├── PowerStone.java
│   │   └── SmokeBomb.java
│   └── effect/
│       ├── StatusEffect.java          <- Interface (shared contract)
│       ├── StunEffect.java
│       ├── DefendBuff.java
│       ├── SmokeBombEffect.java
│       └── ArcaneBlastEffect.java
├── engine/
│   ├── TurnOrderStrategy.java         <- Interface (shared contract)
│   ├── SpeedBasedOrder.java           <- Sorts by speed descending
│   └── BattleEngine.java             <- Round loop, turns, win/lose logic
└── ui/
    ├── GameUI.java                    <- All CLI display + input prompts
    ├── InputHandler.java              <- Input validation
    └── BattleDisplay.java             <- Formatting helpers
```

### Layer Separation (BCE Pattern)

| Layer | Package | Responsibility |
|-------|---------|---------------|
| **Boundary** | `ui` | CLI display, user input, formatting |
| **Control** | `engine` | Battle loop, turn order, win/lose conditions |
| **Entity** | `model.*` | Combatants, actions, items, status effects |
The UI layer never contains game rules. The engine depends on abstractions (interfaces), not concrete classes.

---

## SOLID Principles Applied
| Principle | How It's Applied |
|-----------|-----------------|
| **SRP** | Each class has one job: `BasicAttack` handles attack logic, `GameUI` handles display, `BattleEngine` manages the round loop, etc. |
| **OCP** | New actions or status effects can be added by implementing `Action` / `StatusEffect` without modifying `BattleEngine`. The `getSpecialSkill()` + `needsTarget()` pattern lets the engine handle any future player class. |
| **LSP** | `Warrior`, `Wizard`, `Goblin`, and `Wolf` are all interchangeable as `Combatant` throughout the engine. |
| **ISP** | Interfaces are focused: `Action`, `Item`, `StatusEffect`, `TurnOrderStrategy` each define only what their implementors need. |
| **DIP** | `BattleEngine` depends on `TurnOrderStrategy` (interface), `Action` (interface), and `Combatant` (abstraction). It calls `player.getSpecialSkill()` rather than checking `instanceof`. |

---

## Team Responsibilities
| Person | Package(s) | Key Files |
|--------|-----------|-----------|
| **A** | `model/combatant/` | Combatant, Warrior, Wizard, Goblin, Wolf |
| **B** | `model/action/` | Action, BasicAttack, Defend, ShieldBash, ArcaneBlast |
| **C** | `model/item/` + `model/effect/` | Potion, PowerStone, SmokeBomb, StunEffect, DefendBuff, SmokeBombEffect, ArcaneBlastEffect |
| **D** | `engine/` | BattleEngine, SpeedBasedOrder, TurnOrderStrategy |
| **E** | `ui/` + `Main.java` | GameUI, InputHandler, BattleDisplay, Main |

### Shared Interfaces (agreed by all members)
- `Combatant.java` — base for all characters
- `Action.java` — base for all actions (includes `needsTarget()` default method)
- `Item.java` — base for all items
- `StatusEffect.java` — base for all effects
- `TurnOrderStrategy.java` — base for turn ordering