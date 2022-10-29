import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

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

    public Spell(ImageIcon image, String name, double damage_health, double mana_cost, double duration, double movement_speed, double radius, int piercing, type_t type, Spell subSpell) {
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


    /*
            int critDiv = 0;
            for(int i = 0; i < 8; ++i){
                if(Game.player.critical_stage[i]){
                    ++critDiv;
                }
            }
            double damage = this.damage_health * Game.player.spell_effectiveness * ((Game.player.combo > 0)? Game.player.combo * Game.player.combo_dmg_multiplier: 1) * ((new Random().nextInt(Game.BASE_CRITICAL_CHANCE/critDiv) == 13) ? Game.player.crit_dmg_multiplier : 1);

     */

    public void summonProjectile(GameObject o, double delta_X, double delta_Y /*or mouse_x and mouse_y*/) { //also make a method for removing, so also summon the SubSpell //currently only usable for player
        ArrayList<Character> toHit;
        double damage = 1;
        if (o instanceof Player) {
            if (Game.player.mana - this.mana_cost >= 0) {
                Game.player.mana -= mana_cost;
                toHit = new ArrayList<>(Game.enemies);
                int critDiv = 1;
                for(int i = 0; i < 8; ++i){
                    if(Game.player.critical_stage[i]){
                        critDiv *= 2;
                    }
                }
                damage = Game.player.spell_effectiveness * ((new Random().nextInt(Game.BASE_CRITICAL_CHANCE/critDiv) == 13) ? Game.player.crit_dmg_multiplier : 1);
            } else {
                return;
            }
        } else { // if instanceof Enemy
            ((Enemy) o).mana_recovery_speed = this.mana_cost * 10;
            toHit = new ArrayList<>();
            toHit.add(Game.player);
        }
        double angle;
        if (delta_X == 0) {
            if (delta_Y > 0) {
                angle = 270;
            } else {
                angle = 90;
            }
        } else {
            angle = Math.atan(delta_Y / delta_X) + ((delta_X > 0) ? Math.PI : 0);
        }
        double x = o.x + cos(angle) * o.radius;
        double y = o.y + sin(angle) * o.radius;
        Projectile proj = new Projectile(x, y, angle, this.movement_speed * ((Character) o).spell_effectiveness, this.damage_health * damage, this.duration * ((Character) o).spell_effectiveness, this.piercing, this.radius, this.image.getImage().getScaledInstance((int) radius * 2, (int) radius * 2, Image.SCALE_FAST), this, toHit);
        Game.projectiles.add(Game.projectiles.size(), proj);
    }

    public void summonSubProjectile(double angle, double x, double y, ArrayList oldToHit) {
        if (subSpell != null) {
            ArrayList<Character> toHit;
            if (oldToHit.size() != 0 && oldToHit.get(0) instanceof Player) {
                toHit = new ArrayList<>();
                toHit.add(Game.player);
            }else {
                toHit = new ArrayList<>(Game.enemies);
            }
            Projectile proj = new Projectile(x, y, angle, this.subSpell.movement_speed, this.subSpell.damage_health, this.subSpell.duration, this.subSpell.piercing, this.subSpell.radius, this.subSpell.image.getImage().getScaledInstance((int) this.subSpell.radius * 2, (int) this.subSpell.radius * 2, Image.SCALE_FAST), this.subSpell, toHit);
            Game.projectiles.add(Game.projectiles.size(), proj);
        }

    }
}
