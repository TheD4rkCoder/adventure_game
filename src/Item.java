import javax.swing.*;
import java.awt.*;

public class Item {
    protected ImageIcon icon;
    protected Image image;

    protected String name;
    protected String description;

    Item (Image image, String name, String description){
        this.image = image;
        this.icon = new ImageIcon();
        this.icon.setImage(image.getScaledInstance(100, 100, 0));
        this.name = name;
        this.description = description;
    }

}
