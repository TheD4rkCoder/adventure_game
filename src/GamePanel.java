import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {
    protected int deltaX;
    protected int deltaY;
    protected int screenscrollX, screenscrollY;
    protected ImageIcon background;
    public static double manabarAnimationOffset;

    //protected Inventory inv;

    GamePanel() {
        super();
        this.setPreferredSize(new Dimension(1280, 720));
        background = new ImageIcon("background.png");
        /*
        inv = new Inventory();
        this.add(inv);
        */
    }


    public void paint(Graphics g) {
        Game.centerX = this.getWidth() >> 1; // byteshifting is faster than dividing by 2
        Game.centerY = this.getHeight() >> 1;
        deltaX = (int) (Game.player.x - Game.centerX); // the ingame coordinates of the top left corner
        deltaY = (int) (Game.player.y - Game.centerY);
        // background calculations
        screenscrollX = (deltaX % background.getIconWidth() > 0) ? (-(deltaX % background.getIconWidth())) :  ((-(deltaX % background.getIconWidth()))-background.getIconWidth());
        screenscrollY = (deltaY % background.getIconHeight() > 0) ? (-(deltaY % background.getIconHeight())) :  ((-(deltaY % background.getIconHeight()))-background.getIconHeight());

        Graphics2D g2D = (Graphics2D) g;

        // paint background
        g2D.drawImage(background.getImage(), screenscrollX, screenscrollY, null);
        g2D.drawImage(background.getImage(), screenscrollX + background.getIconWidth(), screenscrollY, null);
        g2D.drawImage(background.getImage(), screenscrollX, screenscrollY + background.getIconHeight(), null);
        g2D.drawImage(background.getImage(), screenscrollX + background.getIconWidth(), screenscrollY + background.getIconHeight(), null);

        //how to get a different sized image:
        //image.getImage().getScaledInstance()

        // draw all projectiles
        for (int i = 0; i < Game.projectiles.size(); i++) {
            GameObject p = Game.projectiles.get(i);
            g2D.drawImage(Game.projectiles.get(i).image, (int) (p.x - p.radius - deltaX), (int) (p.y - p.radius - deltaY), null);

        }

        float alpha = 8 * 0.1f;
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2D.setComposite(alcom);
        // draw all enemies | textures still need to be replaced with images
        g2D.setPaint(Color.green);
        for (int i = 0; i < Game.enemies.size(); i++) {
            GameObject p = Game.enemies.get(i);
            g2D.fillOval((int) (p.x - p.radius - deltaX), (int) (p.y - p.radius - deltaY), (int) p.radius * 2, (int) p.radius * 2);
        }
        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        g2D.setComposite(alcom);

        g2D.setPaint(Color.red);
        // draw player and other gui like health bar
        //g2D.drawRect((int) (Game.player.x-Game.player.radius), (int) (Game.player.y-Game.player.radius), (int)Game.player.radius*2, (int)Game.player.radius*2);
        g2D.fillOval((int) (this.getWidth() / 2 - Game.player.radius), (int) (this.getHeight() / 2 - Game.player.radius), (int) Game.player.radius * 2, (int) Game.player.radius * 2);

        // user GUI:
        g2D.setStroke(new BasicStroke(10));
        // healthbar
        g2D.drawRect(this.getWidth() - 340, this.getHeight() - 80, (int) (250 * Game.player.hp / Game.player.maxHP), 20);
        g2D.setPaint(Color.white);
        g2D.drawRect(this.getWidth() - 350, this.getHeight() - 90, 270, 40);
        //staminabar
        g2D.setPaint(Color.orange);
        g2D.drawRect(this.getWidth() - 340, this.getHeight() - 140, (int) (250 * Game.player.stamina / Game.player.maxStamina), 20);
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
