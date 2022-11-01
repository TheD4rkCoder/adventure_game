import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Projectile extends GameObject {

    protected double movement_speed;
    protected Image image;
    protected double damage;
    protected double durationLeft;
    protected int piercing;

    protected ArrayList<Character> enemiesToHit;

    protected Spell spell;
    protected double angle;

    Projectile(double x, double y, double angle, double movement_speed, double damage, double durationLeft, int piercing, double radius, Image image, Spell spell, ArrayList toHit) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.movement_speed = movement_speed;
        this.damage = damage;
        this.durationLeft = durationLeft;
        this.piercing = piercing;
        this.radius = radius;
        this.image = image;
        this.spell = spell;

        // change
        this.enemiesToHit = toHit;

    }

    public boolean move() {
        this.x = this.x + cos(angle) * movement_speed;
        this.y = this.y + sin(angle) * movement_speed;
        --durationLeft;

        // true = yes, the projectile should still exist


        // afterward do logic so it saves what it has already hit instead of what it still has to hit
        // for this, maybe use "faction" of characters


        for (int i = 0; i < enemiesToHit.size(); ++i) {
            if (Game.collisionCheck(this, enemiesToHit.get(i))) {
                enemiesToHit.get(i).hp -= (int) (damage);
                enemiesToHit.remove(i);
                piercing -= 1;

                spell.summonSubProjectile(angle, x, y, enemiesToHit);

                if (piercing < 1) {
                    return false;
                }
                --i;
            }
        }
        if (durationLeft < 1) {
            spell.summonSubProjectile(angle, x, y, enemiesToHit);
            return false;
        }
        return true;
    }
}
