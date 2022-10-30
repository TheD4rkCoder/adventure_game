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

        this.stage = 1;

        this.intelligence = 1;
        this.endurance = 1;
        this.strength = 1;
        this.dexterity = 1;
        this.wisdom = 1;
        this.levelUp(0);

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

    public void increaseStat(String stat, int amount) {
        switch (stat) {
            case "Intelligence" -> {
                intelligence += amount;
                spell_effectiveness = pow(1.005, intelligence);
                mana_recovery_speed = (((pow(1.03, intelligence) - log(stage)) < 0.1) ? 0.1 : pow(1.03, intelligence) - log(stage));
            }
            case "Strength" -> {
                strength += amount;
                baseDamage = 5 + strength * 2;
                maxHP = 100 + 2.5 * strength + pow(1.01, endurance);
                hp += 2.5 * amount;
                if (hp > maxHP) {
                    hp = maxHP;
                }
                def = strength + 2 * endurance;
                melee_dmg_multiplier = pow(1.005, strength);
                crit_dmg_multiplier = pow(1.0025, strength);
            }
            case "Endurance" -> {
                endurance += amount;
                maxHP = 100 + 2.5 * strength + pow(1.01, endurance);
                hp += pow(1.01, endurance);
                if (hp > maxHP) {
                    hp = maxHP;
                }
                def = strength + 2 * endurance;
                stamina += 3 * amount;
                maxStamina = 100 + 3 * endurance;

            }
            case "Dexterity" -> {
                dexterity += amount;
                combo_dmg_multiplier = pow(1.00004, dexterity);
                movement_speed = 2 + pow(1.002, dexterity);
            }
            default -> wisdom += amount;
        }

    }

    public void printStats() {
        System.out.println("Stage:" + stage);

        System.out.println("Intelligence: " + intelligence);
        System.out.println("mana_rec_speed" + mana_recovery_speed);
        System.out.println("spell effectiveness" + spell_effectiveness);

        System.out.println("strength" + strength);
        System.out.println("Endurance" + endurance);
        System.out.println("BaseDamage" + baseDamage);
        System.out.println("maxHP" + maxHP);
        System.out.println("HP" + hp);
        System.out.println("Defence" + def);
        System.out.println("melee mult" + melee_dmg_multiplier);
        System.out.println("crit mult" + crit_dmg_multiplier);

        System.out.println("dexterity" + dexterity);
        System.out.println("Combo damage" + combo_dmg_multiplier);
        System.out.println("move spd" + movement_speed);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.comboTimer) {
            combo = 0;
        }
    }

    public void addToCombo() {
        if (combo == 0) {
            this.comboTimer.start();
        } else {
            this.comboTimer.setDelay(Game.COMBO_TIMER_BASE_VALUE / (2 * combo));
            this.comboTimer.restart();
        }
        combo += 1;
    }
}