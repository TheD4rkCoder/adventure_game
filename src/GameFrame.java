import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

import static java.lang.Math.sqrt;

public class GameFrame extends JFrame implements ActionListener, KeyListener, MouseListener {

    private GamePanel gamePanel;
    private Timer timer;
    private boolean[] isKeyPressed;

    private Timer enemyCourseAdjust;

    GameFrame(String windowName) {
        super(windowName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setVisible(true);

        this.gamePanel = new GamePanel();
        this.gamePanel.addMouseListener(this);
        this.add(gamePanel);
        this.addKeyListener(this);

        this.isKeyPressed = new boolean[]{false, false, false, false};

        this.timer = new Timer(Game.FRAME_TIME, this);
        this.timer.start();

        this.enemyCourseAdjust = new Timer(Game.ENEMY_COURSE_ADJUST_TIME, this);
        this.enemyCourseAdjust.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.timer) {
            if (Game.player.x_movement != 0 && Game.player.y_movement != 0) {

                Game.player.x += Game.player.x_movement*0.71;
                Game.player.y += Game.player.y_movement*0.71;
            } else {
                Game.player.x += Game.player.x_movement;
                Game.player.y += Game.player.y_movement;
            }

            Random rand = new Random();
            for (int i = 0; i < Game.enemies.size(); i++) {
                for (int j = 0; j < Game.enemies.size(); j++) {
                    if (i != j)
                        Game.collisionCheck(Game.enemies.get(i), Game.enemies.get(j));
                }
            }

            //EnemySpawning
            for (int i = 0; i < rand.nextInt(10); ++i) {
                if (rand.nextInt(500) == 13) {
                    switch (rand.nextInt(4)) {
                        case 0 -> {//north
                            Game.enemies.add(Game.enemies.size(), new Enemy("Test", rand.nextInt(this.getWidth() + 1), -5, rand.nextInt(20), 5, 100));
                        }
                        case 1 -> {//east
                            Game.enemies.add(Game.enemies.size(), new Enemy("Test", this.getWidth() + 5, rand.nextInt(this.getHeight() + 1), rand.nextInt(20), 5, 100));
                        }
                        case 2 -> {//south
                            Game.enemies.add(Game.enemies.size(), new Enemy("Test", rand.nextInt(this.getWidth() + 1), this.getHeight() + 5, rand.nextInt(20), 5, 100));
                        }
                        case 3 -> {//east
                            Game.enemies.add(Game.enemies.size(), new Enemy("Test", -5, rand.nextInt(this.getHeight() + 1), rand.nextInt(20), 5, 100));
                        }
                    }
                }
            }


            //EnemyMovement
            for(int i = 0; i < Game.enemies.size(); ++i){
                if(Game.enemies.get(i).hp <= 0){
                    Game.enemies.remove(i);
                    --i;
                }else{
                    Game.enemies.get(i).moveTowardsPlayer();
                }
            }

            //ProjectileMovement
            for(int i = 0; i < Game.projectiles.size(); ++i){
                if(!Game.projectiles.get(i).move()){
                    Game.projectiles.remove(i);
                    --i;
                }
            }

            gamePanel.repaint();
        } else if (e.getSource() == this.enemyCourseAdjust) {
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

    }

    @Override
    public void mousePressed(MouseEvent e) {

        switch (e.getButton()) {
            case 1: // lmb
                Game.player.attack(e.getX(), e.getY());
                break;
            case 2: // mouse_wheel
                break;
            case 3: // rmb
                break;
            case 4: // undo (browser)
                break;
            case 5: // redo (browser)
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}