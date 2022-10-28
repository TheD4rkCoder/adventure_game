import java.lang.Math;
import java.util.Random;

public class Enemy extends Character{

    private int range;
    protected double distanceToPlayer;
    protected double angleToPlayer;

    public Enemy(String faction, double x, double y, int stage, double baseDamage, int hp) {
        super(faction);
        this.hp = hp;
        this.baseDamage = baseDamage;
        this.range = 50;
        //insert Image
        //insert radius
        this.radius = 25;
        this.x = x;
        this.y = y;
        this.calculateDistanceToPlayer();


        Random rand = new Random();

        intelligence = rand.nextInt(1+(int)(stage*0.4));
        endurance = rand.nextInt(1+(int)(stage*0.4));
        strength = rand.nextInt(1+(int)(stage*0.4));
        dexterity = rand.nextInt(1+(int)(stage*0.4));
        wisdom = rand.nextInt(1+(int)(stage*0.4));


        levelUp("Intelligence", intelligence * 3);
        levelUp("Strength", strength * 3);
        levelUp("Endurance", endurance * 3);
        levelUp("Dexterity", dexterity * 3);
        levelUp("Wisdom", wisdom * 3);

    }

    public void calculateDistanceToPlayer(){
        double deltaX = Game.player.x - this.x;
        double deltaY = Game.player.y - this.y;
        this.distanceToPlayer = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        this.angleToPlayer = Math.atan(deltaY/deltaX) + ((deltaX > 0) ? 0 : Math.PI);
    }
    public void moveTowardsPlayer() {
        if(distanceToPlayer > 739){
            return;
        }

        this.x = this.x + Math.cos(angleToPlayer) * movement_speed;
        this.y = this.y + Math.sin(angleToPlayer) * movement_speed;

        //Collision with Player


        if(distanceToPlayer < range){
            attack();
        }
        Game.collisionCheck(this, Game.player);
    }
    public void attack() {
        //if (Game.player.def < 2 * strength){
            Game.player.hp -= (int)(baseDamage*melee_dmg_multiplier*(new Random().nextInt(1, 10) < 2 ? crit_dmg_multiplier : 1));
        //}
    }
}
