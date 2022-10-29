import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

import static java.lang.Math.log;
import static java.lang.Math.pow;

public class Character extends GameObject implements ActionListener {
    private String faction;

    protected int stage, intelligence, strength, dexterity, wisdom, endurance, def, stamina, maxStamina;

    protected double baseDamage, spell_effectiveness, mana_recovery_speed/*in Enemy, cooldown between casts*/, melee_dmg_multiplier, movement_speed, combo_dmg_multiplier, crit_dmg_multiplier, hp, maxHP;

    protected ArrayList<Spell> spells;
    protected Timer comboTimer;
    protected int combo;


    public Character(String faction) {
        this.faction = faction;

        this.stage = 0;

        this.intelligence = 1;
        this.spell_effectiveness = 1; //P
        this.mana_recovery_speed = 1; //P

        this.endurance = 1;
        this.strength = 1;
        this.maxHP = 100; //P
        this.hp = 100; //P
        this.def = 0; //P
        this.maxStamina = 100; //P
        this.stamina = 100; //P
        this.melee_dmg_multiplier = 1; //E

        this.dexterity = 1;
        this.movement_speed = 1; //I
        this.combo_dmg_multiplier = 1; //I
        this.crit_dmg_multiplier = 1; //E

        this.wisdom = 1;

        this.spells = new ArrayList<>();

        this.comboTimer = new Timer(Game.COMBO_TIMER_BASE_VALUE, this);
        this.combo = 0;

    }
    public void levelUp(int amount) {
        this.stage += amount;
            this.increaseStat("Intelligence", amount);
            this.increaseStat("Strength", amount);
            this.increaseStat("Endurance", amount);
            this.increaseStat("Dexterity", amount);

    }

    public void increaseStat (String stat, int amount){
        switch (stat) {
            case "Intelligence" -> {
                intelligence += amount;
                for (int i = 0; i < amount; ++i) {
                    spell_effectiveness += spell_effectiveness * 0.005;
                    mana_recovery_speed = (((pow(1.03, intelligence) - log(stage)) < 1) ? 1 : pow(1.03, intelligence) - log(stage));
                }
            }
            case "Strength" -> {
                strength += amount;
                baseDamage = 10 + strength * 2.7;
                for (int i = 0; i < amount; ++i) {
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
                endurance += amount;
                for (int i = 0; i < amount; ++i) {
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
                dexterity += amount;
                for (int i = 0; i < amount; ++i) {
                    combo_dmg_multiplier = combo_dmg_multiplier + combo_dmg_multiplier * dexterity / 25 * 0.001;
                    movement_speed = movement_speed + 0.0001;
                }
            }
            default -> wisdom += amount;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.comboTimer){
            combo = 0;
        }
    }

    public void addToCombo (){
        if(combo == 0){
            this.comboTimer.start();
        }else{
            this.comboTimer.setDelay(Game.COMBO_TIMER_BASE_VALUE/(2*combo));
            this.comboTimer.restart();
        }
        combo += 1;
    }
}