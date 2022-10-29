import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.log;
import static java.lang.Math.pow;

public class Player extends Character {
    private double comprehension_speed; // now Wisdom
    //affects advancing/leveling speed
    protected double x_movement;
    protected double y_movement;
    protected double mana, maxMana;

    protected boolean[] critical_stage = new boolean[8]; //Dexterity
    //different requirements to trigger critical hit


    private double item_stat_multiplier; //Wisdom

    private double mastery_multiplier; //Wisdom
    //like elements as mage, skills as warrior and crafting blueprint of tools and items as an alchemist

    public Player(String faction) {
        super(faction);
        this.image = new ImageIcon("player.png");
        this.radius = 20;
        this.movement_speed = 3;
        this.comprehension_speed = 1;
        this.maxHP = 100;
        this.hp = 100;
        this.mana = 100;
        this.maxMana = 100;

        this.spells.add(this.spells.size(), Game.spells[0]);
        this.spells.add(this.spells.size(), Game.spells[1]);
        this.spells.add(this.spells.size(), Game.spells[2]);

        for (int i = 0; i < 8; ++i) {
            this.critical_stage[i] = false;
        }

        this.item_stat_multiplier = 1;

        this.mastery_multiplier = 1;
        this.mana_recovery_speed = 0.1;
    }

    @Override
    public void increaseStat(String stat, int amount) {
        super.increaseStat(stat, amount);
        switch (stat) {
            case "Intelligence" -> {
                maxMana = pow(1.01, intelligence) + intelligence + 10;
            }
            case "Dexterity" -> {
                if (dexterity > 50) {
                    critical_stage[8] = true;
                    if (dexterity > 100) {
                        critical_stage[7] = true;
                        if (dexterity > 300) {
                            critical_stage[6] = true;
                        }
                    }
                }
            }
            case "Wisdom" -> {
                item_stat_multiplier = pow(0.005, wisdom);
                mastery_multiplier = wisdom / 50 + 1;
                comprehension_speed = pow(1.02, wisdom);
            }
        }
    }

    @Override
    public void printStats() {
        System.out.println("Stage" + stage);
        System.out.println("mastery" + mastery_multiplier);
        System.out.println("stats" + item_stat_multiplier);
    }

    public void attack(int mouse_X, int mouse_Y) {
        this.addToCombo();
        this.spells.get(2).summonProjectile(this, Game.centerX - mouse_X, Game.centerY - mouse_Y);
    }

    public void refresh() {
        this.mana += this.mana_recovery_speed;
        if (this.mana > this.maxMana) {
            this.mana = this.maxMana;
        }
        this.stamina += maxStamina / 100;
        if (this.stamina > this.maxStamina) {
            this.stamina = this.maxStamina;
        }
        //System.out.println(this.stamina);
    }
}
