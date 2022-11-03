import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.PI;


public class GameFrame extends JFrame implements ActionListener, KeyListener, MouseListener {

    private GamePanel gamePanel;
    private Timer timer;
    private boolean[] isKeyPressed;
    private boolean isMousePressed;
    public static int mousePressedTime;
    private Timer enemyCourseAdjust;


    GameFrame(String windowName) {
        super(windowName);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);

        this.gamePanel = new GamePanel();
        this.gamePanel.addMouseListener(this);

        this.add(gamePanel);

        this.addKeyListener(this);

        // "start" the window after adding all components
        this.setVisible(true);


        this.isKeyPressed = new boolean[]{false, false, false, false};

        this.timer = new Timer(Game.FRAME_TIME, this);
        this.timer.start();

        this.enemyCourseAdjust = new Timer(Game.ENEMY_COURSE_ADJUST_TIME, this);
        this.enemyCourseAdjust.start();

        //Game.player.inventory.addItem(new Armour(new ImageIcon("img.png").getImage(), "Oha", "Test", 20, 3));

    }

    public void test() {
        Game.player.inventory.addItem(new Armour(new ImageIcon("img.png").getImage(), "Oha", "Test", 20, 3, null));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.timer) {
            // Main game loop
            if (Game.player.stamina > 0) {
                if (Game.player.x_movement != 0 && Game.player.y_movement != 0) {
                    Game.player.x += Game.player.x_movement * 0.71;
                    Game.player.y += Game.player.y_movement * 0.71;
                    Game.player.stamina -= 2;
                } else if (Game.player.x_movement != 0 || Game.player.y_movement != 0) {
                    Game.player.x += Game.player.x_movement;
                    Game.player.y += Game.player.y_movement;
                    Game.player.stamina -= 2;
                }
            }
            Game.player.refresh();
            Game.collisions_and_movements();

            Random rand = new Random();


            //EnemySpawning | later let them spawn in random structures
            for (int i = 0; i < rand.nextInt(10); ++i) {
                if (rand.nextInt(500) == 13) {
                    switch (rand.nextInt(4)) {
                        case 0 -> //north
                                Game.enemies.add(Game.enemies.size(), new Enemy("Test", rand.nextInt(this.getWidth()) + Game.player.x - this.getWidth() / 2, Game.player.y - this.getHeight() / 2, rand.nextInt(20)));
                        case 1 -> //east
                                Game.enemies.add(Game.enemies.size(), new Enemy("Test", this.getWidth() / 2 + Game.player.x, rand.nextInt(this.getHeight() + 1) + Game.player.y - this.getHeight() / 2, rand.nextInt(20)));
                        case 2 -> //south
                                Game.enemies.add(Game.enemies.size(), new Enemy("Test", rand.nextInt(this.getWidth()) + Game.player.x - this.getWidth() / 2, this.getHeight() / 2 + Game.player.y, rand.nextInt(20)));
                        case 3 -> //west
                                Game.enemies.add(Game.enemies.size(), new Enemy("Test", Game.player.x - this.getWidth() / 2, rand.nextInt(this.getHeight() + 1) + Game.player.y - this.getHeight() / 2, rand.nextInt(20)));
                    }
                }
            }
            if (isMousePressed) {
                mousePressedTime++;
            }

            gamePanel.repaint();

        } else if (e.getSource() == this.enemyCourseAdjust) {
            // separate slower loop for some calculations
            for (Enemy en : Game.enemies) {
                en.calculateDistanceToPlayer();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case 87 -> {
                if (!this.isKeyPressed[0]) {
                    Game.player.y_movement -= Game.player.movement_speed;
                    this.isKeyPressed[0] = true;
                } //w
            }
            case 65 -> {
                if (!this.isKeyPressed[1]) {
                    Game.player.x_movement -= Game.player.movement_speed;
                    this.isKeyPressed[1] = true;
                }//a
            }
            case 83 -> {
                if (!this.isKeyPressed[2]) {
                    Game.player.y_movement += Game.player.movement_speed;
                    this.isKeyPressed[2] = true;
                }//s
            }
            case 68 -> {
                if (!this.isKeyPressed[3]) {
                    Game.player.x_movement += Game.player.movement_speed;
                    this.isKeyPressed[3] = true;
                }//d
            }
            case 69 -> {    //e
                if (Game.player.inventory.opened) {
                    Game.player.inventory.close();
                    this.timer.restart();
                } else {
                    this.timer.stop();
                    //this.getGraphics().setClip(0, 0, this.getWidth(), this.getHeight());

                    repaintInventory();
                    Game.player.inventory.open();
                }
            }
            case 81 -> { //q
                if (Game.player.inventory.opened) {
                    Game.player.inventory.dropItem();
                    repaintInventory();
                }

            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 87 -> {
                Game.player.y_movement += Game.player.movement_speed;
                this.isKeyPressed[0] = false;
            }
            case 65 -> {
                Game.player.x_movement += Game.player.movement_speed;
                this.isKeyPressed[1] = false;
            }
            case 83 -> {
                Game.player.y_movement -= Game.player.movement_speed;
                this.isKeyPressed[2] = false;
            }
            case 68 -> {
                Game.player.x_movement -= Game.player.movement_speed;
                this.isKeyPressed[3] = false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (Game.player.inventory.opened) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            if (Game.player.pointsAvailable > 0) {
                if (Game.player.inventory.levelUps[0].isIn(mouseX + 7, mouseY + 30)) {
                    Game.player.increaseStat("Intelligence", 1);
                } else if (Game.player.inventory.levelUps[1].isIn(mouseX + 7, mouseY + 30)) {
                    Game.player.increaseStat("Strength", 1);
                } else if (Game.player.inventory.levelUps[2].isIn(mouseX + 7, mouseY + 30)) {
                    Game.player.increaseStat("Endurance", 1);
                } else if (Game.player.inventory.levelUps[3].isIn(mouseX + 7, mouseY + 30)) {
                    Game.player.increaseStat("Dexterity", 1);
                } else if (Game.player.inventory.levelUps[4].isIn(mouseX + 7, mouseY + 30)) {
                    Game.player.increaseStat("Wisdom", 1);
                }
            }
            for (int i = 0; i < 9; ++i) {
                if (Game.player.inventory.itemSelection[i].isIn(mouseX + 7, mouseY + 30)) {
                    Item temp = Game.player.inventory.items[i];
                    Game.player.inventory.items[i] = Game.player.inventory.tempItem;
                    Game.player.inventory.tempItem = temp;
                }

            }
            for (int i = 0; i < 3; ++i) {
                if (Game.player.inventory.hotBarSelection[i].isIn(mouseX + 7, mouseY + 30)) {
                    Item temp = Game.player.inventory.hotBar[i];
                    Game.player.inventory.hotBar[i] = Game.player.inventory.tempItem;
                    Game.player.inventory.tempItem = temp;
                }

            }
            if (Game.player.inventory.armourSelection.isIn(mouseX + 7, mouseY + 30)) {
                if (Game.player.inventory.tempItem instanceof Armour || Game.player.inventory.tempItem == null) {
                    if (Game.player.inventory.armourSlot != null) {
                        Game.player.maxHP -= ((Armour) Game.player.inventory.armourSlot).hpBuff;
                        Game.player.def -= ((Armour) Game.player.inventory.armourSlot).defenceBuff;
                    }
                    Item temp = Game.player.inventory.armourSlot;
                    Game.player.inventory.armourSlot = Game.player.inventory.tempItem;
                    Game.player.inventory.tempItem = temp;
                    if (Game.player.inventory.armourSlot != null) {
                        Game.player.maxHP += ((Armour) Game.player.inventory.armourSlot).hpBuff;
                        Game.player.def += ((Armour) Game.player.inventory.armourSlot).defenceBuff;
                    }
                    if (Game.player.hp > Game.player.maxHP) {
                        Game.player.hp = Game.player.maxHP;
                    }
                }
            }
            repaintInventory();
        }
    }

    public void repaintInventory() {
        Graphics g = this.getGraphics();
        g.setClip(0, 0, this.getWidth(), this.getHeight()); //clip stores the size of the frame/window
        Game.player.inventory.paint(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        switch (e.getButton()) {
            case 1: // lmb
                if (!Game.player.inventory.opened) {
                    if (Game.player.inventory.hotBar[0] != null && Game.player.inventory.hotBar[0].attack != null) {
                        Game.player.inventory.hotBar[0].attack.summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY());
                    }
                }
                break;
            case 2: // mouse_wheel
                break;
            case 3: // rmb
                isMousePressed = true;
                break;
            case 4: // undo (browser)
                if (!Game.player.inventory.opened) {
                    if (Game.player.inventory.hotBar[2] != null && Game.player.inventory.hotBar[2].attack != null) {
                        Game.player.inventory.hotBar[2].attack.summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY());
                    }
                }
                break;
            case 5: // redo (browser)
                if (!Game.player.inventory.opened) {
                    if (Game.player.inventory.hotBar[1] != null && Game.player.inventory.hotBar[1].attack != null) {
                        Game.player.inventory.hotBar[1].attack.summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY());
                    }
                }
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1: // lmb
                break;
            case 2: // mouse_wheel
                break;
            case 3: // rmb
                isMousePressed = false;
                if (mousePressedTime < 50) {
                    if (!Game.player.inventory.opened) {
                        Game.player.attack(e.getX(), e.getY());
                    }
                } else {
                    // select spell
                    //(Game.centerX - 120 + (int) (cos(PI / 2 + i * 2 * PI / Game.player.spells.size()) * 80), Game.centerY - 120 - (int) (sin(PI / 2 + i * 2 * PI / Game.player.spells.size()) * 80), 240, 240, (int) (90 + (i - 0.5) * 360 / Game.player.spells.size()), 360 / Game.player.spells.size());
                    for (int i = 0, size = Game.player.spells.size(); i < size; i++) {
                        double temp_X = Game.centerX + (int) (cos(PI / 2 + i * 2 * PI / size) * 80);
                        double temp_Y = Game.centerY - (int) (sin(PI / 2 + i * 2 * PI / size) * 80);
                        double distance = sqrt(pow(e.getX() - temp_X, 2) + pow(e.getY() - temp_Y, 2));
                        double borderAngle = PI / 2 + (i - 0.5) * 2 * PI / size;
                        if (borderAngle > 2 * PI) {
                            borderAngle -= 2 * PI;
                        }
                        double angleToMouse;
                        if (e.getX() - temp_X == 0) {
                            if (e.getY() - temp_Y > 0) {
                                angleToMouse = 1.5 * PI;
                            } else {
                                angleToMouse = 0.5 * PI;
                            }
                        } else {
                            angleToMouse = atan((temp_Y - e.getY()) / (e.getX() - temp_X)) + ((temp_X - e.getX() > 0) ? PI : 0);
                            if (angleToMouse < 0) {
                                angleToMouse += 2 * PI;
                            }
                        }
                        if (distance > 68 && distance < 173 && ((angleToMouse > borderAngle && angleToMouse < borderAngle + 2 * PI / Game.player.spells.size()) || (angleToMouse > borderAngle - 2 * PI && angleToMouse < borderAngle - 2 * PI + 2 * PI / Game.player.spells.size()))) {
                            Game.player.selectedSpell = i;
                        }
                    }
                }
                mousePressedTime = 0;
                break;
            case 4: // undo (browser)
                break;
            case 5: // redo (browser)
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}