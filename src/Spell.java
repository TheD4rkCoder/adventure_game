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

    public boolean summonProjectile(GameObject o, double delta_X, double delta_Y) {
        double damage = 1;
        double effectiveness = 1;
        String faction = "";
        if (o instanceof Character) {
            if (o instanceof Player) {
                if (type == type_t.physical) {
                    if (this.mana_cost <= Game.player.stamina) {
                        Game.player.stamina -= this.mana_cost;
                        damage *= Game.player.melee_dmg_multiplier;
                    } else {
                        return false;
                    }
                } else if (Game.player.mana + Game.player.stamina * 0.1 - this.mana_cost >= 0) {
                    damage *= Game.player.spell_effectiveness;
                    if (Game.player.mana - mana_cost < 0) {
                        double cost = mana_cost - Game.player.mana;
                        Game.player.mana = 0;
                        Game.player.stamina -= cost * 10;
                    } else {
                        Game.player.mana -= mana_cost;
                    }
                } else {
                    return false;
                }
            } else { // if instanceof Enemy
                ((Character) o).mana_recovery_speed = this.mana_cost * 10;
                if (this.type == type_t.physical) {
                    damage *= ((Character) o).melee_dmg_multiplier;
                } else {
                    damage *= ((Character) o).spell_effectiveness;
                }
            }
            damage *= ((new Random().nextInt((int) ((Character) o).critrate) == 0) ? ((Character) o).crit_dmg_multiplier : 1);
            effectiveness *= pow(((Character) o).spell_effectiveness, 0.3);
            faction = ((Character) o).faction;
        } else if (o instanceof Projectile) {
            faction = ((Projectile)o).faction;
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

        Projectile proj = new Projectile(x, y, angle, this.movement_speed * effectiveness, this.damage_health * damage, this.duration * effectiveness, this.piercing, this.radius, this.type, this.image.getImage().getScaledInstance((int) radius * 2, (int) radius * 2, Image.SCALE_FAST), this, faction);
        Game.projectiles.add(Game.projectiles.size(), proj);
        return true;
    }

    public void summonSubProjectile(Projectile p) {
        if (subSpell != null) {
            this.subSpell.summonProjectile(p, cos(p.angle), sin(p.angle));
        }
        //Projectile proj = new Projectile(x, y, angle, this.subSpell.movement_speed, this.subSpell.damage_health, this.subSpell.duration, this.subSpell.piercing, this.subSpell.radius, type, this.subSpell.image.getImage().getScaledInstance((int) this.subSpell.radius * 2, (int) this.subSpell.radius * 2, Image.SCALE_FAST), this.subSpell, faction);
        //Game.projectiles.add(Game.projectiles.size(), proj);
    }

}

