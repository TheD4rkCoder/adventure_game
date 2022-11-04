import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Projectile extends GameObject {

    protected double movement_speed;
    protected Image image;
    protected double damage;
    protected double durationLeft;
    protected int piercing;

    protected ArrayList<Character> enemiesHit;
    protected String faction;

    protected Spell spell;
    protected double angle;
    Spell.type_t type;

    Projectile(double x, double y, double angle, double movement_speed, double damage, double durationLeft, int piercing, double radius, Spell.type_t type, Image image, Spell spell, String faction) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.movement_speed = movement_speed;
        this.damage = damage;
        this.durationLeft = durationLeft;
        this.piercing = piercing;
        this.radius = radius;
        this.type = type;
        this.image = image;
        this.spell = spell;

        // change
        this.faction = faction;
        this.enemiesHit = new ArrayList<>();

    }

    public boolean move() {
        this.x = this.x + cos(angle) * movement_speed;
        this.y = this.y + sin(angle) * movement_speed;
        --durationLeft;

        // true = yes, the projectile should still exist
        if (this.piercing > 0) {
            if (this.faction.equals("hostile")) {
                if (this.enemiesHit.size() == 0) {
                    if (Game.collisionCheck(this, Game.player)) {
                        this.enemiesHit.add(Game.player);
                        double damageDealt = this.damage * 0.2 * (6 - (pow(6, Game.player.def / this.damage)));
                        if (damageDealt > 0) {
                            Game.player.hp -= damageDealt;
                        }
                        this.piercing--;
                        this.spell.summonSubProjectile(this); // faction = "hostile"
                    }
                }
            } else {
                for (int i = 0; i < Game.enemies.size(); ++i) {
                    boolean check = false;
                    for (int j = 0; j < this.enemiesHit.size(); j++) {
                        if (Game.enemies.get(i).equals(this.enemiesHit.get(j))) {
                            check = true;
                        }
                    }
                    if (!check && Game.collisionCheck(this, Game.enemies.get(i))) {
                        enemiesHit.add(Game.enemies.get(i));
                        double damageDealt = this.damage * 0.2 * (6 - (pow(6, Game.enemies.get(i).def / this.damage)));
                        if (damageDealt > 0) {
                            Game.enemies.get(i).hp -= damageDealt;
                        }
                        this.piercing--;
                        this.spell.summonSubProjectile(this); // faction = "friendly"
                    }
                }
            }
        } else if (this.movement_speed != 0) {
            return false;
        }
        if (durationLeft < 1) {
            this.spell.summonSubProjectile(this);
            return false;
        }
        return true;
    }
}
