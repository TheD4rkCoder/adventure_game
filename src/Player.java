import javax.swing.*;
import java.util.Arrays;

import static java.lang.Math.*;

public class Player extends Character {
    protected double comprehension_speed; // now Wisdom
    //affects advancing/leveling speed
    protected double x_movement, y_movement;
    protected double mana, maxMana;

    protected boolean[] critical_stage = new boolean[8]; //Dexterity
    //different requirements to trigger critical hit
    protected double item_stat_multiplier, mastery_multiplier; //Wisdom
    //like elements as mage, skills as warrior and crafting blueprint of tools and items as an alchemist
    public int selectedSpell = 0;

    protected int pointsAvailable;
    protected Inventory inventory;

    public Player(String faction) {
        super(faction);
        this.hp = maxHP;
        this.image = new ImageIcon("img_1.png");
        this.radius = 20;

        this.spells.add(this.spells.size(), Game.spells[0]);
        this.spells.add(this.spells.size(), Game.spells[1]);
        this.spells.add(this.spells.size(), Game.spells[2]);
        this.spells.add(this.spells.size(), Game.spells[3]);

        for (int i = 0; i < 8; ++i) {
            this.critical_stage[i] = false;
        }
        this.levelUp(10);

        this.inventory = new Inventory();
        this.pointsAvailable = 20;
    }

    @Override
    public void increaseStat(String stat, int amount) {
        super.increaseStat(stat, amount);
        switch (stat) {
            case "Intelligence" -> maxMana = pow(1.01, intelligence) + intelligence + 10;
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
        this.pointsAvailable -= amount;
    }

    @Override
    public void printStats() {
        super.printStats();
        System.out.println( "Player{" +
                "comprehension_speed=" + comprehension_speed +
                ", x_movement=" + x_movement +
                ", y_movement=" + y_movement +
                ", mana=" + mana +
                ", maxMana=" + maxMana +
                ", critical_stage=" + Arrays.toString(critical_stage) +
                ", item_stat_multiplier=" + item_stat_multiplier +
                ", mastery_multiplier=" + mastery_multiplier +
                '}');
    }


    public void attack(int mouse_X, int mouse_Y) {
        this.addToCombo();
        this.spells.get(selectedSpell).summonProjectile(this, Game.centerX - mouse_X, Game.centerY - mouse_Y);
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
