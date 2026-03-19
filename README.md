# SC2002 Turn-Based Combat Arena

## Quick Start for Team Members

### One-time setup
```bash
git clone <your-repo-url>
cd SC2002-Combat-Arena
```

### Daily workflow (only 3 commands!)
```bash
git pull                          # 1. always pull first
# ... do your coding ...
git add -A && git commit -m "describe what you did"   # 2. save your work
git push                          # 3. push to repo
```

### Golden rule: ONLY edit files in YOUR folder.

---

## Who owns what

| Person | Package folder(s) | Files to create |
|--------|-------------------|----------------|
| **A** | `src/model/combatant/` | Warrior, Wizard, Goblin, Wolf, Player, Enemy classes |
| **B** | `src/model/action/` | BasicAttack, Defend, ShieldBash, ArcaneBlast, UseItem, CooldownTracker |
| **C** | `src/model/item/` + `src/model/effect/` | Potion, PowerStone, SmokeBomb, StunEffect, DefendBuff, SmokeBombEffect, ArcaneBlastBuff |
| **D** | `src/engine/` | BattleEngine, SpeedBasedOrder, Level, GameManager, SpawnManager |
| **E** | `src/ui/` + `src/Main.java` | GameUI, InputHandler, BattleDisplay, Main |

---

## Shared interfaces (DO NOT EDIT — agreed by everyone)

These files in the root of each package are the **contracts**.
Everyone codes to these. If you need to change one, message the group chat first.

- `src/model/combatant/Combatant.java` — base for all characters
- `src/model/action/Action.java` — base for all actions
- `src/model/item/Item.java` — base for all items
- `src/model/effect/StatusEffect.java` — base for all effects
- `src/engine/TurnOrderStrategy.java` — base for turn ordering

---

## How to compile and run
```bash
cd src
javac Main.java
java Main
```
