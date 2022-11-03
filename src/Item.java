import javax.swing.*;
import java.awt.*;

public class Item extends GameObject{

    protected Image image;
    protected String name;
    protected String description;
    public Spell attack;

    Item (Image image, String name, String description, Spell attack){
        this.image = image;
        this.icon = new ImageIcon();
        this.icon.setImage(image.getScaledInstance(100, 100, 0));
        this.name = name;
        this.description = description;
        this.attack = attack;
        this.radius = 50;
    }

}
