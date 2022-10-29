import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Player extends Character {
    private double comprehension_speed; //Intelligence
    //affects advancing/leveling speed
    protected double x_movement;
    protected double y_movement;
    protected double mana, maxMana;
    protected ArrayList<Spell> spells;

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
        this.spells = new ArrayList<>();
        this.spells.add(this.spells.size(), Game.spells[0]);
        this.spells.add(this.spells.size(), Game.spells[1]);
        this.spells.add(this.spells.size(), Game.spells[2]);


        for (int i = 0; i < 8; ++i) {
            this.critical_stage[i] = false;
        }

        this.item_stat_multiplier = 1;

        this.mastery_multiplier = 1;
        this.mana_recovery_speed = 1;
    }

    @Override
    public void levelUp(String stat, int levels) {
        super.levelUp(stat, levels);
    }

    public void attack(int mouse_X, int mouse_Y) {
        this.spells.get(2).summonProjectile(mouse_X, mouse_Y);
    }
    public void refresh() {
        this.mana += this.mana_recovery_speed;
        if (this.mana > this.maxMana) {
            this.mana = this.maxMana;
        }
    }
}
