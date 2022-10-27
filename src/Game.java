import javax.swing.*;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Game {
    static Player player = new Player("o7");
    static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    static ArrayList<GameObject> obstacles = new ArrayList<GameObject>();//chage this once we have obstacle spells
    static ArrayList<Wall> walls = new ArrayList<Wall>();
    static ArrayList<Projectile> projectiles = new ArrayList<>();
    static Spell[] spells = new Spell[] {
            new Spell(new ImageIcon("manaBolt.png"), "Mana Bolt", 10, 5, null, 100, 5, 50, 2, Spell.type_t.projectile)
    };

    static final int FRAME_TIME = 5; //how long a Frame is in milliseconds
    static final int ENEMY_COURSE_ADJUST_TIME = 100; //how long between the adjustments of the enemies course
    static double centerX, centerY;

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
                    case 0:
                        o1.x = wall.x - o1.radius;
                        break;
                    case 1:
                        o1.x = wall.x + wall.width + o1.radius;
                        break;
                    case 2:
                        o1.y = wall.y - o1.radius;
                        break;
                    case 3:
                        o1.y = wall.y + wall.height + o1.radius;
                        break;
                }
                return true;
            }

            return false;
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
            return false;
        }
    }
}
