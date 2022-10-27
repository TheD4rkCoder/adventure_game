import javax.swing.*;
import java.awt.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Spell {
    protected ImageIcon image;
    protected String name;
    protected double damage_health;
    protected double mana_cost;
    protected Spell subSpell; // spell that spawns after the projectile of this spell gets removed
    protected double duration;
    protected double movement_speed;
    protected double radius;
    protected int piercing;

    protected enum type_t {
        projectile, obstacle, regeneration
    }

    protected type_t type;

    public Spell(ImageIcon image, String name, double damage_health, double mana_cost, Spell subSpell, double duration, double movement_speed, double radius, int piercing, type_t type) {
        this.image = image;
        this.name = name;
        this.damage_health = damage_health;
        this.mana_cost = mana_cost;
        this.subSpell = subSpell;
        this.duration = duration;
        this.movement_speed = movement_speed;
        this.radius = radius;
        this.piercing = piercing;
        this.type = type;
    }

    public void summonProjectile(int mouse_X, int mouse_Y) { //also make a method for removing, so also sumon the subspell
        if (Game.player.mana - mana_cost >= 0) {
            Game.player.mana -= mana_cost;
            GamePanel.manabarAnimationOffset += mana_cost;
            Projectile proj = new Projectile(mouse_X, mouse_Y, this.movement_speed, this.damage_health, this.duration, this.piercing, this.radius, this.image.getImage().getScaledInstance((int)radius*2, (int)radius*2, Image.SCALE_FAST), this);
            Game.projectiles.add(Game.projectiles.size(), proj);
        }

    }
    public void summonSubProjectile(double angle) {
        if (subSpell != null) {
            Projectile proj = new Projectile(Game.centerX + cos(angle), Game.centerY + sin(angle), this.subSpell.movement_speed, this.subSpell.damage_health, this.subSpell.duration, this.piercing, this.radius, this.subSpell.image.getImage().getScaledInstance((int) radius * 2, (int) radius * 2, Image.SCALE_FAST), this.subSpell);
            Game.projectiles.add(Game.projectiles.size(), proj);
        }

    }
}
