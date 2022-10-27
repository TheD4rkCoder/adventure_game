import static java.lang.Math.log;
import static java.lang.Math.pow;

public class Character extends GameObject{
    private String faction;

    protected int stage, intelligence, strength, endurance, def, stamina, dexterity, wisdom;

    protected double baseDamage, spell_effectiveness, mana_recovery_speed, melee_dmg_multiplier, movement_speed, combo_dmg_multiplier, crit_dmg_multiplier, hp, maxHP;


    public Character(String faction) {
        this.faction = faction;

        this.stage = 0;

        this.intelligence = 0;
        this.spell_effectiveness = 1;
        this.mana_recovery_speed = 1;

        this.endurance = 0;
        this.strength = 0;
        this.hp = 100;
        this.def = 100;
        this.stamina = 100;
        this.melee_dmg_multiplier = 1;

        this.dexterity = 0;
        this.movement_speed = 1;
        this.combo_dmg_multiplier = 1;
        this.crit_dmg_multiplier = 1;

        this.wisdom = 0;
    }


    public void levelUp (String stat, int levels){
        stage += 4;
        float stat_multiplier = (float)(stage*1.5);
        //if intelligence
        ++intelligence;
        spell_effectiveness = spell_effectiveness * 1.1f;
        mana_recovery_speed = (float)(pow(1.03, intelligence) - (int) (log(stage)));
    }
    public void printStats (){
        System.out.println("Stage:" + stage);
        System.out.println("Intelligence: " + intelligence);
        System.out.println("mana_rec_speed" + mana_recovery_speed);
        System.out.println("spell effectiveness" + spell_effectiveness);
        System.out.println("strength" + strength);
        System.out.println("Hp" + hp);
    }
}