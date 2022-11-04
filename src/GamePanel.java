import javax.swing.*;
import java.awt.*;

import static java.lang.Math.*;

public class GamePanel extends JPanel {
    protected int deltaX;
    protected int deltaY;
    protected int screenscrollX, screenscrollY;
    protected ImageIcon background;
    public static double manabarAnimationOffset;


    GamePanel() {
        super();
        this.setPreferredSize(new Dimension(1280, 720));
        background = new ImageIcon("background.png");
    }


    public void paint(Graphics g) {
        Game.centerX = this.getWidth() >> 1; // byteshifting is faster than dividing by 2
        Game.centerY = this.getHeight() >> 1;
        deltaX = (int) (Game.player.x - Game.centerX); // the ingame coordinates of the top left corner
        deltaY = (int) (Game.player.y - Game.centerY);
        // background calculations
        screenscrollX = (deltaX % background.getIconWidth() > 0) ? (-(deltaX % background.getIconWidth())) : ((-(deltaX % background.getIconWidth())) - background.getIconWidth());
        screenscrollY = (deltaY % background.getIconHeight() > 0) ? (-(deltaY % background.getIconHeight())) : ((-(deltaY % background.getIconHeight())) - background.getIconHeight());

        Graphics2D g2D = (Graphics2D) g;

        // paint background
        g2D.drawImage(background.getImage(), screenscrollX, screenscrollY, null);
        g2D.drawImage(background.getImage(), screenscrollX + background.getIconWidth(), screenscrollY, null);
        g2D.drawImage(background.getImage(), screenscrollX, screenscrollY + background.getIconHeight(), null);
        g2D.drawImage(background.getImage(), screenscrollX + background.getIconWidth(), screenscrollY + background.getIconHeight(), null);

        // draw all items
        g2D.setPaint(Color.BLACK);
        for (int i = 0; i < Game.itemsLayingAround.size(); i++) {
            GameObject p = Game.itemsLayingAround.get(i);
            g2D.fillRect((int) (p.x - p.radius - deltaX - 10), (int) (p.y - p.radius - deltaY - 10), (int) (2 * p.radius + 20), (int) (2 * p.radius + 20));
            g2D.drawImage(Game.itemsLayingAround.get(i).icon.getImage(), (int) (p.x - p.radius - deltaX), (int) (p.y - p.radius - deltaY), null);
        }

        // draw all projectiles
        for (int i = 0; i < Game.projectiles.size(); i++) {
            if (Game.projectiles.get(i).type != Spell.type_t.buff) {
                GameObject p = Game.projectiles.get(i);
                g2D.drawImage(Game.projectiles.get(i).image, (int) (p.x - p.radius - deltaX), (int) (p.y - p.radius - deltaY), null);
            }
        }
        // draw all enemies | textures still need to be replaced with images
        g2D.setPaint(Color.green);
        for (int i = 0; i < Game.enemies.size(); i++) {
            GameObject p = Game.enemies.get(i);
            g2D.fillOval((int) (p.x - p.radius - deltaX), (int) (p.y - p.radius - deltaY), (int) p.radius * 2, (int) p.radius * 2);
        }

        // draw player
        g2D.drawImage(Game.player.icon.getImage(), (int) (Game.centerX - Game.player.radius), (int) (Game.centerY - Game.player.radius), null);

        // user GUI:
        g2D.setStroke(new BasicStroke(10));
        // healthbar
        g2D.setPaint(Color.red);
        g2D.drawRect(this.getWidth() - 340, this.getHeight() - 80, (int) (250 * Game.player.hp / Game.player.maxHP), 20);
        g2D.setPaint(Color.white);
        g2D.drawRect(this.getWidth() - 350, this.getHeight() - 90, 270, 40);
        //staminabar
        g2D.setPaint(Color.orange);
        g2D.drawRect(this.getWidth() - 340, this.getHeight() - 140, 250 * Game.player.stamina / Game.player.maxStamina, 20);
        g2D.setPaint(Color.white);
        g2D.drawRect(this.getWidth() - 350, this.getHeight() - 150, 270, 40);
        // manabar
        if (manabarAnimationOffset * Game.player.maxMana > Game.player.mana) {
            g2D.setPaint(Color.cyan);
            g2D.drawRect(this.getWidth() - 340, this.getHeight() - 200, (int) (250 * manabarAnimationOffset), 20);
            manabarAnimationOffset -= 0.001;
        } else {
            manabarAnimationOffset = Game.player.mana / Game.player.maxMana;
        }
        g2D.setPaint(Color.blue);
        g2D.drawRect(this.getWidth() - 340, this.getHeight() - 200, (int) (250 * Game.player.mana / Game.player.maxMana), 20);

        g2D.setPaint(Color.white);
        g2D.drawRect(this.getWidth() - 350, this.getHeight() - 210, 270, 40);

        // hotBar
        g2D.setPaint(Color.BLACK);
        g2D.fillRect(this.getWidth() - 720, this.getHeight() - 165, 340, 120);
        g2D.setFont(new Font("Arial", Font.PLAIN, 20));
        for (int i = 0; i < 3; ++i) {
            if (Game.player.inventory.hotBar[i] == null) {
                g2D.setPaint(Color.GRAY);
                g2D.fillRect(this.getWidth() - 710 + i * 110, this.getHeight() - 155, 100, 100);
            } else {
                g2D.drawImage(Game.player.inventory.hotBar[i].icon.getImage(), this.getWidth() - 710 + i * 110, this.getHeight() - 155, null);
                g2D.setPaint(Color.white);
                g2D.drawString(String.format("%d", Game.player.inventory.hotBar[i].amount), this.getWidth() - 700 + i * 110, this.getHeight() - 135);

            }
        }

        // spell choosing circle
        if (GameFrame.mousePressedTime > 20) {
            float alpha = 7 * 0.1f;
            AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2D.setComposite(alcom);
            g2D.setStroke(new BasicStroke(105));
            int size = 0;
            for (int i = 0; i < 9; i++) {
                if (Game.player.spellInventory.items[i] != null && Game.player.spellInventory.items[i].attack != null) {
                    size++;
                }
            }
            for (int i = 0; i < size; i++) {
                g2D.setPaint(Color.DARK_GRAY);
                if (i == Game.player.selectedSpellInt) {
                    g2D.setPaint(Color.LIGHT_GRAY);
                }
                g2D.drawArc(Game.centerX - 120 + (int) (cos(PI / 2 + i * 2 * PI / size) * 80), Game.centerY - 120 - (int) (sin(PI / 2 + i * 2 * PI / size) * 80), 240, 240, (int) (90 + (i - 0.5) * 360 / size), 360 / size);
            }
            for (int j = 0, i = 0; j < 9; j++) {
                if (Game.player.spellInventory.items[j] != null) {
                    if (Game.player.spellInventory.items[j].attack != null) {
                            g2D.drawImage(Game.player.spellInventory.items[j].attack.image.getImage(), (int) (cos(PI / 2 + i * PI * 2 / size) * 200 + Game.centerX - 50), (int) (sin(-PI / 2 + i * 2 * PI / size) * 200 + Game.centerY - 50), null);
                            i++;
                    }
                }
            }
            alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
            g2D.setComposite(alcom);
        }


        // some useful commands:
        //g2D.drawImage(image, 0, 0, null);

        //g2D.setPaint(Color.pink);
        //g2D.drawRect(0, 0, 100, 200);
        //g2D.fillRect(0, 0, 100, 200);

        //g2D.setPaint(Color.orange);
        //g2D.drawOval(0, 0, 100, 100);
        //g2D.fillOval(0, 0, 100, 100);
        //g2D.fillArc(0, 0, 100, 100, 180, 180);

        //int[] xPoints = {150,250,350};
        //int[] yPoints = {300,150,300};
        //g2D.drawPolygon(xPoints, yPoints, 3);
        //g2D.fillPolygon(xPoints, yPoints, 3);

        //g2D.setFont(new Font("Ink Free",Font.BOLD,50));
        //g2D.drawString("U R A WINNER! :D", 50, 50);
    }
}
