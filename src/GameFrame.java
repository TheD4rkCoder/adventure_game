import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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


            //EnemySpawning | later let them spawn in random structures
            for (int i = 0; i < Game.random.nextInt(10); ++i) {
                if (Game.random.nextInt(500) == 13) {
                    switch (Game.random.nextInt(4)) {
                        case 0 -> //north
                                Game.enemies.add(Game.enemies.size(), new Enemy("hostile", Game.random.nextInt(this.getWidth()) + Game.player.x - this.getWidth() / 2, Game.player.y - this.getHeight() / 2, Game.random.nextInt(20), new Item[]{Item.random_weapon(), Item.random_weapon()}));
                        case 1 -> //east
                                Game.enemies.add(Game.enemies.size(), new Enemy("hostile", this.getWidth() / 2 + Game.player.x, Game.random.nextInt(this.getHeight() + 1) + Game.player.y - this.getHeight() / 2, Game.random.nextInt(20), new Item[]{Item.random_weapon(), Item.random_weapon()}));
                        case 2 -> //south
                                Game.enemies.add(Game.enemies.size(), new Enemy("hostile", Game.random.nextInt(this.getWidth()) + Game.player.x - this.getWidth() / 2, this.getHeight() / 2 + Game.player.y, Game.random.nextInt(20), new Item[]{Item.random_weapon(), Item.random_weapon()}));
                        case 3 -> //west
                                Game.enemies.add(Game.enemies.size(), new Enemy("hostile", Game.player.x - this.getWidth() / 2, Game.random.nextInt(this.getHeight() + 1) + Game.player.y - this.getHeight() / 2, Game.random.nextInt(20), new Item[]{Item.random_weapon(), Item.random_weapon()}));
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
            case 81 -> { //q
                if (Game.player.inventory.opened) {
                    Game.player.inventory.dropItem();
                    repaintInventory();
                } else if (Game.player.spellInventory.opened) {
                    Game.player.spellInventory.dropItem();
                    repaintInventory();
                }
            }
            case 73 -> { //i
                if (Game.player.inventory.opened) {
                    Game.player.inventory.showItemStats = Game.player.inventory.tempItem;
                } else if (Game.player.spellInventory.opened) {
                    Game.player.spellInventory.showItemStats = Game.player.spellInventory.tempItem;
                }
                repaintInventory();
            }
            case 27 -> {//esc
                if(Game.player.inventory.opened){
                    Game.player.inventory.close();
                    gamePanel.repaint();
                    gamePanel.pauseMenuOpened = true;
                    gamePanel.repaint();
                }else if(Game.player.spellInventory.opened){
                    Game.player.spellInventory.close();
                    gamePanel.repaint();
                    gamePanel.pauseMenuOpened = true;
                    gamePanel.repaint();
                }else if(gamePanel.pauseMenuOpened){
                    gamePanel.pauseMenuOpened = false;
                    gamePanel.repaint();
                    gamePanel.painted = false;
                    gamePanel.saved = false;
                    gamePanel.notAvailable = false;
                    this.timer.restart();
                }else{
                    this.timer.stop();
                    //gamePanel.repaint();
                    gamePanel.pauseMenuOpened = true;
                    gamePanel.repaint();
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gamePanel.pauseMenuOpened) {
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
                case 69 -> {//e
                    if (Game.player.inventory.opened) {
                        Game.player.inventory.close();
                        this.timer.restart();
                    } else if (!Game.player.spellInventory.opened) {
                        this.timer.stop();
                        Game.player.inventory.open();
                        repaintInventory();
                    }
                }
                case 82 -> {    //r
                    if (Game.player.spellInventory.opened) {
                        Game.player.spellInventory.close();
                        this.timer.restart();
                    } else if (!Game.player.inventory.opened) {
                        this.timer.stop();
                        Game.player.spellInventory.open();
                        repaintInventory();
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(gamePanel.pauseMenuOpened){
            int mouseX = e.getX();
            int mouseY = e.getY();
            if(gamePanel.saveButton.isIn(mouseX, mouseY)){
                Game.save();
                gamePanel.saved = true;
                gamePanel.notAvailable = false;
                gamePanel.repaint();
                return;
            }
            if(gamePanel.loadButton.isIn(mouseX, mouseY)){
                gamePanel.writing = true;
                gamePanel.saved = false;
                gamePanel.repaint();
                gamePanel.loadInput.setVisible(true);
                gamePanel.loadInput.setEnabled(true);
                return;
            }
            if(gamePanel.continueButton.isIn(mouseX, mouseY)){
                gamePanel.pauseMenuOpened = false;
                gamePanel.repaint();
                gamePanel.painted = false;
                gamePanel.saved = false;
                gamePanel.notAvailable = false;
                this.timer.restart();
                this.requestFocus();
                return;
            }
        }
        for (int k = 0; k < 2; k++) {
            Inventory inv;
            if (k == 0) {
                inv = Game.player.inventory;
            } else {
                inv = Game.player.spellInventory;
            }
            if (inv.opened) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                if (k == 0) {
                    if (Game.player.pointsAvailable > 0) {
                        if (inv.levelUps[0].isIn(mouseX + 7, mouseY + 30)) {
                            Game.player.increaseStat("Intelligence", 1);
                        } else if (inv.levelUps[1].isIn(mouseX + 7, mouseY + 30)) {
                            Game.player.increaseStat("Strength", 1);
                        } else if (inv.levelUps[2].isIn(mouseX + 7, mouseY + 30)) {
                            Game.player.increaseStat("Endurance", 1);
                        } else if (inv.levelUps[3].isIn(mouseX + 7, mouseY + 30)) {
                            Game.player.increaseStat("Dexterity", 1);
                        } else if (inv.levelUps[4].isIn(mouseX + 7, mouseY + 30)) {
                            Game.player.increaseStat("Wisdom", 1);
                        }
                    }
                }
                for (int i = 0; i < inv.items.length; ++i) {
                    if (inv.itemSelection[i].isIn(mouseX + 7, mouseY + 30)) {
                        if (e.getButton() == 1) {
                            if (inv.items[i] != null && inv.tempItem != null && inv.items[i].equals(inv.tempItem)) {
                                inv.items[i].amount += inv.tempItem.amount;
                                inv.tempItem = null;
                            } else {
                                Item temp = inv.items[i];
                                inv.items[i] = inv.tempItem;
                                inv.tempItem = temp;
                            }
                        } else if (e.getButton() == 3 && inv.items[i] != null) {
                            if (inv.tempItem == null) {
                                if (!(inv.items[i] instanceof Armour)) {
                                    //inv.tempItem = new Item(inv.items[i].image, inv.items[i].name, 1, inv.items[i].description, inv.items[i].attack, inv.items[i].consumable);
                                    inv.tempItem = new Item(inv.items[i].imageValues, inv.items[i].source, inv.items[i].name, 1, inv.items[i].description, inv.items[i].attack, inv.items[i].consumable);
                                } else {
                                    Armour temp = (Armour) inv.items[i];
                                    //inv.tempItem = new Armour(temp.image, temp.name, temp.description, temp.hpBuff, temp.defenceBuff, temp.attack);
                                    inv.tempItem = new Armour(temp.imageValues, temp.source, temp.name, temp.description, temp.hpBuff, temp.defenceBuff, temp.attack);
                                }
                                inv.items[i].amount--;
                            } else if (inv.items[i].equals(inv.tempItem)) {
                                inv.items[i].amount--;
                                inv.tempItem.amount++;
                            }
                            if (inv.items[i].amount < 1) {
                                inv.items[i] = null;
                            }
                        } else if (e.getButton() == 2) {
                            inv.showItemStats = inv.items[i];
                        }
                    }

                }
                for (int i = 0; i < inv.hotBar.length; ++i) {
                    if (inv.hotBarSelection[i].isIn(mouseX + 7, mouseY + 30)) {
                        if (e.getButton() == 1) {
                            if (inv.hotBar[i] != null && inv.tempItem != null && inv.hotBar[i].equals(inv.tempItem)) {
                                inv.hotBar[i].amount += inv.tempItem.amount;
                                inv.tempItem = null;
                            } else {
                                Item temp = inv.hotBar[i];
                                inv.hotBar[i] = inv.tempItem;
                                inv.tempItem = temp;
                            }
                        } else if (e.getButton() == 3 && inv.hotBar[i] != null) {
                            if (inv.tempItem == null) {
                                if (!(inv.hotBar[i] instanceof Armour)) {
                                    //inv.tempItem = new Item(inv.hotBar[i].image, inv.hotBar[i].name, 1, inv.hotBar[i].description, inv.hotBar[i].attack, inv.hotBar[i].consumable);
                                    inv.tempItem = new Item(inv.hotBar[i].imageValues, inv.hotBar[i].source, inv.hotBar[i].name, 1, inv.hotBar[i].description, inv.hotBar[i].attack, inv.hotBar[i].consumable);
                                } else {
                                    Armour temp = (Armour) inv.hotBar[i];
                                    //inv.tempItem = new Armour(temp.image, temp.name, temp.description, temp.hpBuff, temp.defenceBuff, temp.attack);
                                    inv.tempItem = new Armour(temp.imageValues, temp.source, temp.name, temp.description, temp.hpBuff, temp.defenceBuff, temp.attack);
                                }
                                inv.hotBar[i].amount--;
                            } else if (inv.tempItem.equals(inv.hotBar[i])) {
                                inv.hotBar[i].amount--;
                                inv.tempItem.amount++;
                            }
                            if (inv.hotBar[i].amount < 1) {
                                inv.hotBar[i] = null;
                            }
                        } else if (e.getButton() == 2) {
                            inv.showItemStats = inv.hotBar[i];
                        }
                    }

                }
                if (inv.armourSelection.isIn(mouseX + 7, mouseY + 30)) {
                    if (e.getButton() == 2) {
                        inv.showItemStats = inv.armourSlot;
                    } else if (inv.tempItem instanceof Armour || inv.tempItem == null) {
                        if (inv.armourSlot != null) {
                            Game.player.maxHP -= ((Armour) inv.armourSlot).hpBuff;
                            Game.player.def -= ((Armour) inv.armourSlot).defenceBuff;
                        }
                        Item temp = inv.armourSlot;
                        inv.armourSlot = inv.tempItem;
                        inv.tempItem = temp;
                        if (inv.armourSlot != null) {
                            Game.player.maxHP += ((Armour) inv.armourSlot).hpBuff;
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
    }

    public void repaintInventory() {
        Graphics g = this.getGraphics();
        g.setClip(0, 0, this.getWidth(), this.getHeight()); //clip stores the size of the frame/window
        if (Game.player.inventory.opened) {
            Game.player.inventory.paint(g);
        } else if (Game.player.spellInventory.opened) {
            Game.player.spellInventory.paint(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (!Game.player.inventory.opened && !Game.player.spellInventory.opened) {
            switch (e.getButton()) {
                case 1: // lmb
                    if (Game.player.inventory.hotBar[0] != null && Game.player.inventory.hotBar[0].attack != null) {
                        Game.player.inventory.hotBar[0].attack.summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY(), true);
                    } else {
                        if (Game.spells[0].summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY(), false)) {
                            Game.projectiles.get(Game.projectiles.size() - 1).damage = Game.player.baseDamage;

                        }
                    }
                    break;
                case 2: // mouse_wheel
                    break;
                case 3: // rmb
                    isMousePressed = true;
                    break;
                case 4: // undo (browser)
                    if (Game.player.inventory.hotBar[2] != null && Game.player.inventory.hotBar[2].attack != null) {
                        Game.player.inventory.hotBar[2].attack.summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY(), true);
                    } else {
                        if (Game.spells[0].summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY(), false)) {
                            Game.projectiles.get(Game.projectiles.size() - 1).damage = Game.player.baseDamage;
                        }

                    }
                    break;
                case 5: // redo (browser)
                    if (Game.player.inventory.hotBar[1] != null && Game.player.inventory.hotBar[1].attack != null) {
                        Game.player.inventory.hotBar[1].attack.summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY(), true);
                    } else {
                        if (Game.spells[0].summonProjectile(Game.player, Game.centerX - e.getX(), Game.centerY - e.getY(), false)) {
                            Game.projectiles.get(Game.projectiles.size() - 1).damage = Game.player.baseDamage;
                        }
                    }
                    break;
            }
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
                if (mousePressedTime < 20) {
                    if (!Game.player.inventory.opened && !Game.player.spellInventory.opened) {
                        Game.player.attack(e.getX(), e.getY());
                    }
                } else {
                    // select spell
                    //(Game.centerX - 120 + (int) (cos(PI / 2 + i * 2 * PI / Game.player.spells.size()) * 80), Game.centerY - 120 - (int) (sin(PI / 2 + i * 2 * PI / Game.player.spells.size()) * 80), 240, 240, (int) (90 + (i - 0.5) * 360 / Game.player.spells.size()), 360 / Game.player.spells.size());
                    int size = 0;
                    for (int i = 0; i < 9; i++) {
                        if (Game.player.spellInventory.items[i] != null) {
                            if (Game.player.spellInventory.items[i].attack != null) {
                                size++;
                            }
                        }
                    }
                    for (int i = 0; i < size; i++) {
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
                        if (distance > 68 && distance < 173 && ((angleToMouse > borderAngle && angleToMouse < borderAngle + 2 * PI / size) || (angleToMouse > borderAngle - 2 * PI && angleToMouse < borderAngle - 2 * PI + 2 * PI / size))) {
                            Game.player.selectedSpellInt = i;
                            for (int j = 0; j < 9; j++) {
                                if (Game.player.spellInventory.items[j] != null && Game.player.spellInventory.items[j].attack != null) {
                                    i--;
                                }
                                if (i == -1) {
                                    Game.player.selectedSpell = Game.player.spellInventory.items[j].attack;
                                    break;
                                }
                            }
                            break;
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