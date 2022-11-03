import java.lang.Math;
import java.util.Random;

public class Enemy extends Character {

    private int range;
    protected double distanceToPlayer;
    protected double angleToPlayer;

    public Enemy(String faction, double x, double y, int stage) {
        super(faction);
        this.range = 150;
        //insert Image
        this.radius = 25;
        this.x = x;
        this.y = y;
        this.calculateDistanceToPlayer();

        this.spells.add(this.spells.size(), Game.spells[3]);

        levelUp(stage);
        this.hp = maxHP;
    }

    public void calculateDistanceToPlayer() {
        double deltaX = Game.player.x - this.x;
        double deltaY = Game.player.y - this.y;
        this.distanceToPlayer = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        this.angleToPlayer = Math.atan(deltaY / deltaX) + ((deltaX > 0) ? 0 : Math.PI);
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "\nrange=" + range +
                "\n, distanceToPlayer=" + distanceToPlayer +
                "\n, angleToPlayer=" + angleToPlayer +
                "\n, stage=" + stage +
                "\n, intelligence=" + intelligence +
                "\n, strength=" + strength +
                "\n, dexterity=" + dexterity +
                "\n, wisdom=" + wisdom +
                "\n, endurance=" + endurance +
                "\n, def=" + def +
                "\n, stamina=" + stamina +
                "\n, maxStamina=" + maxStamina +
                "\n, baseDamage=" + baseDamage +
                "\n, spell_effectiveness=" + spell_effectiveness +
                "\n, mana_recovery_speed=" + mana_recovery_speed +
                "\n, melee_dmg_multiplier=" + melee_dmg_multiplier +
                "\n, movement_speed=" + movement_speed +
                "\n, combo_dmg_multiplier=" + combo_dmg_multiplier +
                "\n, crit_dmg_multiplier=" + crit_dmg_multiplier +
                "\n, hp=" + hp +
                "\n, maxHP=" + maxHP +
                "\n, spells=" + spells +
                "\n, comboTimer=" + comboTimer +
                "\n, combo=" + combo +
                "\n, image=" + icon +
                "\n, x=" + x +
                "\n, y=" + y +
                "\n, radius=" + radius +
                "}\n\n\n";
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
