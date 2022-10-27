import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Projectile extends GameObject{

    protected double movement_speed;
    protected Image image;
    protected double damage;
    protected double durationLeft;
    protected int piercing;

    protected ArrayList<Enemy> enemiesToHit;

    protected Spell spell;
    protected double angle;
    Projectile (double mouseX, double mouseY, double movement_speed, double damage, double durationLeft, int piercing, double radius, Image image, Spell spell){
        double deltaX = Game.centerX - mouseX; // mouseX is relative to the window, not relative to the panel!!!
        double deltaY = Game.centerY - mouseY;
        this.radius = radius;

        this.angle = Math.atan(deltaY/deltaX) + ((deltaX > 0) ? Math.PI : 0);
        this.movement_speed = movement_speed;
        this.x = Game.player.x + cos(angle) * this.radius/2;
        this.y = Game.player.y + sin(angle) * this.radius/2;

        enemiesToHit = new ArrayList<>(Game.enemies);
        this.damage = damage;
        this.durationLeft = durationLeft;
        this.piercing = piercing;
        this.image = image;
        this.spell = spell;
    }

    public boolean move (){
        this.x = this.x + cos(angle) * movement_speed;
        this.y = this.y + sin(angle) * movement_speed;
        if(piercing != 0) {
            for (int i = 0; i < enemiesToHit.size(); ++i) {
                if (Game.collisionCheck(this, enemiesToHit.get(i))) {
                    enemiesToHit.get(i).hp -= (int) (damage);
                    enemiesToHit.remove(i);
                    piercing -= 1;
                    spell.summonSubProjectile(angle);

                    if(piercing < 1){
                        return false;
                    }
                    --i;
                }
            }
        }
        --durationLeft;
        return !(durationLeft < 1);
    }
}
