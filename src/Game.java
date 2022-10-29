import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Game {
    // load sprite sheet:
    final static private BufferedImage sprite_sheet;

    static {
        try {
            sprite_sheet = ImageIO.read(new File("sprite_sheet.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // list of all spells (skills) that exist
    static Spell[] spells = new Spell[]{
            new Spell(new ImageIcon(sprite_sheet.getSubimage(32, 0, 32, 32)), "Mana Bolt", 10, 5, 100, 5, 10, 2, Spell.type_t.projectile, null),
            new Spell(new ImageIcon("lavapool.png"), "Lava Pool", 10, 5, 200, 1, 1000, 5, Spell.type_t.projectile, null),
            new Spell(new ImageIcon(sprite_sheet.getSubimage(0, 0, 32, 32)), "Fireball", 30, 20, 100, 4, 30, 3, Spell.type_t.projectile, new Spell(new ImageIcon(sprite_sheet.getSubimage(0, 0, 32, 32)), "Fireball", 20, 0, 10, 0, 100, 5, Spell.type_t.projectile, null)),
            new Spell(new ImageIcon(sprite_sheet.getSubimage(32, 0, 32, 32)), "Sword swing", 10, 5, 10, 0, 80, 4, Spell.type_t.projectile, null),

    };
    static Player player = new Player("o7");
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static ArrayList<GameObject> obstacles = new ArrayList<>();//change this once we have obstacle spells
    static ArrayList<Wall> walls = new ArrayList<>();
    static ArrayList<Projectile> projectiles = new ArrayList<>();


    static final int FRAME_TIME = 5; //how long a Frame is in milliseconds
    static final int ENEMY_COURSE_ADJUST_TIME = 100; //how long between the adjustments of the enemies course
    static final int COMBO_TIMER_BASE_VALUE = 1000;
    static final int BASE_CRITICAL_CHANCE = 80;
    static int centerX, centerY;

    static public boolean collisionCheck(GameObject o1, GameObject o2 /*if you want to check collision with a wall, the wall always comes second*/) {
        if (o1 instanceof Wall) {
            // does not work with angles
            Wall wall = (Wall) o2;
            if (o1.x + o1.radius > wall.x && o1.x - o1.radius < o2.x + wall.width && o1.y + o1.radius > wall.y && o1.y - o1.radius < wall.y + wall.height) {
                double[] dist = new double[4];
                dist[0] = o1.x - wall.x; // x west
                dist[1] = o2.x + wall.width - o1.x; // x east
                dist[2] = o1.y - wall.y; // y north
                dist[3] = o2.y + wall.height - o1.y; // y south
                int smallest = 0;
                for (int i = 1; i < 4; i++) {
                    if (dist[smallest] > dist[i]) {
                        smallest = i;
                    }
                }
                switch (smallest) {
                    case 0 -> o1.x = wall.x - o1.radius;
                    case 1 -> o1.x = wall.x + wall.width + o1.radius;
                    case 2 -> o1.y = wall.y - o1.radius;
                    case 3 -> o1.y = wall.y + wall.height + o1.radius;
                }
                return true;
            }

        } else {
            // every other object is round, so we don't need differentiation
            if (sqrt(pow(o1.x - o2.x, 2) + pow(o1.y - o2.y, 2)) <= o1.radius + o2.radius) {
                if (!(o1 instanceof Projectile)) {
                    double deltaX = o1.x - o2.x;
                    double deltaY = o1.y - o2.y;
                    double distance = sqrt(pow(deltaX, 2) + pow(deltaY, 2));
                    double angle = Math.atan(deltaY / deltaX) + ((deltaX > 0) ? 0 : Math.PI);
                    double reset = (o1.radius + o2.radius - distance + 1) / 2;

                    o1.x = o1.x + Math.cos(angle) * reset;
                    o1.y = o1.y + Math.sin(angle) * reset;
                    o2.x = o2.x - Math.cos(angle) * reset;
                    o2.y = o2.y - Math.sin(angle) * reset;
                }
                return true;
            }
        }
        return false;
    }

    static public void collisions_and_movements() {
        // EnemyMovement; collision with player is in .move() method
        for (int i = 0; i < Game.enemies.size(); i++) {
            if (Game.enemies.get(i).hp <= 0) {
                Game.enemies.remove(i);
                --i;
            } else {
                Game.enemies.get(i).moveTowardsPlayer();
            }
        }
        // EnemyCollision
        for (int i = 0; i < Game.enemies.size(); i++) {
            for (int j = 0; j < Game.enemies.size(); j++) {
                if (i != j)
                    Game.collisionCheck(Game.enemies.get(i), Game.enemies.get(j));
            }
        }
        // ProjectileMovement; ProjectileCollision is in their .move() method
        for (int i = 0; i < Game.projectiles.size(); i++) {
            if (!Game.projectiles.get(i).move()) {
                Game.projectiles.remove(i);
                --i;
            }
        }
    }
}
