import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

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
        projectile, physical, aoe, obstacle, regeneration
    }

    protected type_t type;

    public Spell(ImageIcon image, String name, double damage_health, double mana_cost, double duration, double movement_speed, double radius, int piercing, type_t type, Spell subSpell) {
        this.image = new ImageIcon(image.getImage().getScaledInstance(100, 100, Image.SCALE_REPLICATE));
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

    public void summonProjectile(GameObject o, double delta_X, double delta_Y /*or mouse_x and mouse_y*/) { //also make a method for removing, so also summon the SubSpell //currently only usable for player
        String faction;
        double damage = 1;
        if (o instanceof Player) {

            if (type == type_t.physical) {
                if (this.mana_cost <= Game.player.stamina) {
                    Game.player.stamina -= this.mana_cost;
                } else {
                    return;
                }
            } else if (Game.player.mana + Game.player.stamina * 0.1 - this.mana_cost >= 0) {

                if (Game.player.mana - mana_cost < 0) {
                    double cost = mana_cost - Game.player.mana;
                    Game.player.mana = 0;
                    Game.player.stamina -= cost * 10;
                } else {
                    Game.player.mana -= mana_cost;
                }
            } else {
                return;
            }
            faction = "friendly";
            int critDiv = 1;
            for (int i = 0; i < 8; ++i) {
                if (Game.player.critical_stage[i]) {
                    critDiv *= 2;
                }
            }
            damage = Game.player.spell_effectiveness * ((new Random().nextInt(Game.BASE_CRITICAL_CHANCE / critDiv) == 13) ? Game.player.crit_dmg_multiplier : 1);

        } else { // if instanceof Enemy
            ((Enemy) o).mana_recovery_speed = this.mana_cost * 10;
            faction = "hostile";
        }
        double angle;
        if (delta_X == 0) {
            if (delta_Y > 0) {
                angle = 1.5 * PI;
            } else {
                angle = 0.5 * PI;
            }
        } else {
            angle = Math.atan(delta_Y / delta_X) + ((delta_X > 0) ? PI : 0);
        }
        double x = o.x + cos(angle) * (this.radius - o.radius);
        double y = o.y + sin(angle) * (this.radius - o.radius);
        Projectile proj = new Projectile(x, y, angle, this.movement_speed * ((Character) o).spell_effectiveness, this.damage_health * damage, this.duration * ((Character) o).spell_effectiveness, this.piercing, this.radius, this.type, this.image.getImage().getScaledInstance((int) radius * 2, (int) radius * 2, Image.SCALE_FAST), this, faction);
        Game.projectiles.add(Game.projectiles.size(), proj);
    }

    public void summonSubProjectile(double angle, double x, double y, String faction) {
        if (subSpell != null) {
            Projectile proj = new Projectile(x, y, angle, this.subSpell.movement_speed, this.subSpell.damage_health, this.subSpell.duration, this.subSpell.piercing, this.subSpell.radius, type, this.subSpell.image.getImage().getScaledInstance((int) this.subSpell.radius * 2, (int) this.subSpell.radius * 2, Image.SCALE_FAST), this.subSpell, faction);
            Game.projectiles.add(Game.projectiles.size(), proj);
        }

    }
}
