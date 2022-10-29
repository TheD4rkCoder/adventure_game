import javax.swing.*;
import java.awt.*;
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

    public void summonProjectile(int mouse_X, int mouse_Y) { //also make a method for removing, so also summon the SubSpell //currently only usable for player
        if (Game.player.mana - mana_cost >= 0) {
            int critDiv = 0;
            for(int i = 0; i < 8; ++i){
                if(Game.player.critical_stage[i]){
                    ++critDiv;
                }
            }
            double damage = this.damage_health * Game.player.spell_effectiveness * ((Game.player.combo > 0)? Game.player.combo * Game.player.combo_dmg_multiplier: 1) * ((new Random().nextInt(Game.BASE_CRITICAL_CHANCE/critDiv) == 13) ? Game.player.crit_dmg_multiplier : 1);
            Game.player.mana -= mana_cost;
            GamePanel.manabarAnimationOffset += mana_cost;
            if(GamePanel.manabarAnimationOffset > Game.player.maxMana){
                GamePanel.manabarAnimationOffset = Game.player.maxMana;
            }
            Projectile proj = new Projectile(mouse_X, mouse_Y, this.movement_speed * Game.player.spell_effectiveness, damage, this.duration * Game.player.spell_effectiveness, this.piercing, this.radius, this.image.getImage().getScaledInstance((int)radius*2, (int)radius*2, Image.SCALE_FAST), this);
            Game.projectiles.add(Game.projectiles.size(), proj);
        }

    }
    public void summonSubProjectile(double angle, double x, double y) {
        if (subSpell != null) {
            Projectile proj = new Projectile(Game.centerX + cos(angle), Game.centerY + sin(angle), this.subSpell.movement_speed, this.subSpell.damage_health, this.subSpell.duration, this.subSpell.piercing, this.subSpell.radius, this.subSpell.image.getImage().getScaledInstance((int) this.subSpell.radius * 2, (int) this.subSpell.radius * 2, Image.SCALE_FAST), this.subSpell);
            proj.x = x;
            proj.y = y;
            Game.projectiles.add(Game.projectiles.size(), proj);
        }

    }
}
