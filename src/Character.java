import static java.lang.Math.log;
import static java.lang.Math.pow;

public class Character extends GameObject{
    private String faction;

    protected int stage, intelligence, strength, endurance, def, stamina, dexterity, wisdom, maxStamina;

    protected double baseDamage, spell_effectiveness, mana_recovery_speed/*in Enemy, cooldown between casts*/, melee_dmg_multiplier, movement_speed, combo_dmg_multiplier, crit_dmg_multiplier, hp, maxHP;


    public Character(String faction) {
        this.faction = faction;

        this.stage = 0;

        this.intelligence = 0;
        this.spell_effectiveness = 1; //I
        this.mana_recovery_speed = 1; //I

        this.endurance = 0;
        this.strength = 0;
        this.maxHP = 100; //I
        this.hp = 100; //I
        this.def = 0; //I
        this.maxStamina = 100;
        this.stamina = 100;
        this.melee_dmg_multiplier = 1;

        this.dexterity = 0;
        this.movement_speed = 1;
        this.combo_dmg_multiplier = 1;
        this.crit_dmg_multiplier = 1;

        this.wisdom = 0;

    }


    public void levelUp (String stat, int levels){
        stage += levels;
        switch (stat) {
            case "Intelligence" -> {
                intelligence += levels;
                for (int i = 0; i < levels * 3; ++i) {
                    spell_effectiveness = spell_effectiveness + spell_effectiveness * 0.005;
                    mana_recovery_speed = (((pow(1.03, intelligence) - log(stage)) < 1) ? 1 : pow(1.03, intelligence) - log(stage));
                }
            }
            case "Strength" -> {
                strength += levels;
                baseDamage = 10 + strength * 2.7;
                for (int i = 0; i < levels * 3; ++i) {
                    maxHP = maxHP + 2.5;
                    hp += 2.5;
                    if (hp > maxHP) {
                        hp = maxHP;
                    }
                    def += 1;
                    melee_dmg_multiplier = melee_dmg_multiplier + melee_dmg_multiplier * 0.005;
                    crit_dmg_multiplier = crit_dmg_multiplier * 1.0025;
                }
            }
            case "Endurance" -> {
                endurance += levels;
                for (int i = 0; i < levels * 3; ++i) {
                    maxHP = maxHP + 5;
                    hp += 5;
                    if (hp > maxHP) {
                        hp = maxHP;
                    }
                    def += 2;
                    stamina += 1;
                    maxStamina += 3;
                }
            }
            case "Dexterity" -> {
                dexterity += levels;
                for (int i = 0; i < levels * 3; ++i) {
                    combo_dmg_multiplier = combo_dmg_multiplier + combo_dmg_multiplier * dexterity / 25 * 0.001;
                    movement_speed = movement_speed + 0.0001;
                }
            }
            default -> wisdom += levels;
        }

    }
    public void printStats (){
        System.out.println("Stage:" + stage);
        
        //System.out.println("Intelligence: " + intelligence);
        //System.out.println("mana_rec_speed" + mana_recovery_speed);
        //System.out.println("spell effectiveness" + spell_effectiveness);
        
        //System.out.println("strength" + strength);
        //System.out.println("Endurance" + endurance);
        //System.out.println("BaseDamage" + baseDamage);
        //System.out.println("maxHP" + maxHP);
        //System.out.println("HP" + hp);
        //System.out.println("Defence" + def);
        //System.out.println("melee mult" + melee_dmg_multiplier);
        //System.out.println("crit mult" + crit_dmg_multiplier);
        
        //System.out.println("dexterity" + dexterity);
        //System.out.println("Combo damage" + combo_dmg_multiplier);
        //System.out.println("move spd" + movement_speed);
    }
}