import java.lang.Math;
import java.util.Random;

public class Enemy extends Character {

    private int range;
    protected double distanceToPlayer;
    protected double angleToPlayer;

    public Enemy(String faction, double x, double y, int stage, double baseDamage, int hp) {
        super(faction);
        this.hp = hp;
        this.baseDamage = baseDamage;
        this.range = 150;
        //insert Image
        //insert radius
        this.radius = 25;
        this.x = x;
        this.y = y;
        this.calculateDistanceToPlayer();

        this.spells.add(this.spells.size(), Game.spells[3]);

        Random rand = new Random();

        intelligence = rand.nextInt(1 + (int) (stage * 0.4));
        endurance = rand.nextInt(1 + (int) (stage * 0.4));
        strength = rand.nextInt(1 + (int) (stage * 0.4));
        dexterity = rand.nextInt(1 + (int) (stage * 0.4));
        wisdom = rand.nextInt(1 + (int) (stage * 0.4));


        levelUp(3);

    }

    public void calculateDistanceToPlayer() {
        double deltaX = Game.player.x - this.x;
        double deltaY = Game.player.y - this.y;
        this.distanceToPlayer = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        this.angleToPlayer = Math.atan(deltaY / deltaX) + ((deltaX > 0) ? 0 : Math.PI);
    }

    public void moveTowardsPlayer() {
        if (distanceToPlayer > 739) {
            return;
        }

        this.x = this.x + Math.cos(angleToPlayer) * movement_speed;
        this.y = this.y + Math.sin(angleToPlayer) * movement_speed;

        //Collision with Player


        if (distanceToPlayer < range && mana_recovery_speed < 0) {
            attack();
        }
        this.mana_recovery_speed--;

        Game.collisionCheck(this, Game.player);
    }

    public void attack() {
        this.addToCombo();
        this.spells.get(0).summonProjectile(this, this.x - Game.player.x, this.y - Game.player.y);

    }
}
