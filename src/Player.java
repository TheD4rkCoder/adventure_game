import javax.swing.*;
import java.awt.*;

public class Player extends Character {
    private double comprehension_speed; //Intelligence
    //affects advancing/leveling speed
    protected double x_movement;
    protected double y_movement;
    protected double mana, maxMana;


    private boolean[] critical_stage = new boolean[8]; //Dexterity
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

        for (int i = 0; i < 8; ++i) {
            this.critical_stage[i] = false;
        }

        this.item_stat_multiplier = 1;

        this.mastery_multiplier = 1;
    }

    @Override
    public void levelUp(String stat, int levels) {
        super.levelUp(stat, levels);
    }

    public void attack(int mouse_X, int mouse_Y) {
        Game.spells[0].summonProjectile(mouse_X, mouse_Y);
    }
}
