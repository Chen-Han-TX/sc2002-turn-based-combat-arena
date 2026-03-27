import java.util.List;

public class Defend implements Action{
    public String getName() {
        return "Defend";
    }

    public void execute(Combatant performer, Combatant target, List<Combatant> allEnemies) {

        int bonusAmount = 10;
        int duration = 2; 

        StatusEffect defenseBuff = new DefenseBuff(duration, bonusAmount);
        
        performer.addStatusEffect(defenseBuff);
        
        System.out.println(performer.getName() + " takes a defensive stance! Defense increased by " + bonusAmount + ".");
    }

    public boolean isAvailable(Combatant performer) {
        return true;
    }
}
