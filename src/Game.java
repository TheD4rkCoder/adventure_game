import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class Game {
    static Random random = new Random();
    // load sprite sheet:
    final static public BufferedImage old_sprite_sheet;
    final static public BufferedImage weapons_sprite_sheet;
    final static public BufferedImage armour_sprite_sheet;
    final static public BufferedImage wands_and_books_sprite_sheet;

    static {
        try {
            wands_and_books_sprite_sheet = ImageIO.read(new File("wands_and_books.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            weapons_sprite_sheet = ImageIO.read(new File("weapons.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            armour_sprite_sheet = ImageIO.read(new File("armour.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    static {
        try {
            old_sprite_sheet = ImageIO.read(new File("sprite_sheet.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // list of all spells (skills) that exist
    static Spell[] spells = new Spell[]{
            new Spell(new ImageIcon(old_sprite_sheet.getSubimage(32 * 3, 32, 32, 32)), "punch", 5, 5, 10, 0, 30, 2, Spell.type_t.physical, null),
            new Spell(new ImageIcon(old_sprite_sheet.getSubimage(32, 0, 32, 32)), "Mana Bolt", 10, 5, 50, 8, 10, 2, Spell.type_t.projectile, null),
            new Spell(new ImageIcon("lavapool.png"), "Lava Pool", 10, 5, 200, 0, 100, 5, Spell.type_t.projectile, null),
            new Spell(new ImageIcon(old_sprite_sheet.getSubimage(0, 0, 32, 32)), "Fireball", 30, 20, 50, 5, 30, 3, Spell.type_t.projectile, new Spell(new ImageIcon(old_sprite_sheet.getSubimage(0, 0, 32, 32)), "Explosion", 20, 0, 10, 0, 100, 5, Spell.type_t.projectile, null)),
            new Spell(new ImageIcon(old_sprite_sheet.getSubimage(32, 0, 32, 32)), "Sword swing", 10, 20, 10, 0, 80, 4, Spell.type_t.physical, null),

    };
    static Player player = new Player("friendly");
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static ArrayList<GameObject> obstacles = new ArrayList<>();//change this once we have obstacle spells
    static ArrayList<Wall> walls = new ArrayList<>();
    static ArrayList<Projectile> projectiles = new ArrayList<>();
    static ArrayList<Item> itemsLayingAround = new ArrayList<>();

    static final int FRAME_TIME = 20; //how long a Frame is in milliseconds
    static final int ENEMY_COURSE_ADJUST_TIME = 100; //how long between the adjustments of the enemies course
    static final int COMBO_TIMER_BASE_VALUE = 1000;
    static final int BASE_CRITICAL_CHANCE = 80;
    static int centerX, centerY;

    static public boolean collisionCheck(GameObject o1, GameObject o2, boolean should_push /*if you want to check collision with a wall, the wall always comes second*/) {
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
                if (should_push) {
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
            if (enemies.get(i).hp <= 0) {
                for (int j = 0; j < enemies.get(i).items.length; j++) {
                    double random_angle = PI * Game.random.nextInt(100) / 50;
                    Item tempItem = enemies.get(i).items[j];
                    tempItem.x = enemies.get(i).x + cos(random_angle) * (tempItem.radius + Game.player.radius + 30);
                    tempItem.y = enemies.get(i).y + sin(random_angle) * (tempItem.radius + Game.player.radius + 30);
                    Game.itemsLayingAround.add(tempItem);
                }
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
                    Game.collisionCheck(Game.enemies.get(i), Game.enemies.get(j), true);
            }
        }
        // ProjectileMovement; ProjectileCollision is in their .move() method
        for (int i = 0; i < Game.projectiles.size(); i++) {
            if (!Game.projectiles.get(i).move()) {
                Game.projectiles.remove(i);
                --i;
            }
        }
        // Item pickup/ collision with items
        for (int i = 0; i < Game.itemsLayingAround.size(); i++) {
            if (collisionCheck(player, itemsLayingAround.get(i), false)) {
                if (player.inventory.addItem(itemsLayingAround.get(i))) {
                    itemsLayingAround.remove(i);
                }
            }
        }
    }
}
