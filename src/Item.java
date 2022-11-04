import javax.swing.*;
import java.awt.*;

public class Item extends GameObject{

    protected Image image;
    protected String name;
    protected String description;
    public Spell attack;
    int amount;

    Item (Image image, String name,int amount, String description, Spell attack){
        this.image = image;
        this.icon = new ImageIcon();
        this.icon.setImage(image.getScaledInstance(100, 100, 0));
        this.name = name;
        this.description = description;
        this.attack = attack;
        this.radius = 50;
        this.amount = amount;
    }
    static public Item random_weapon() {
        Spell attack;
        if (Game.random.nextInt(2) == 0) {
            attack = new Spell(new ImageIcon(Game.old_sprite_sheet.getSubimage(32 * 5, 32, 32, 32)), "Sword swing", Game.random.nextInt(20) + 5, 20, 10, 0, 80, 2, Spell.type_t.physical, null);
        } else {
            attack = new Spell(new ImageIcon(Game.old_sprite_sheet.getSubimage(32, 0, 32, 32)), "Mana Bolt", Game.random.nextInt(20) + 5, 20, 50, 8, 20, 2, Spell.type_t.projectile, null);
        }
        Item weapon = new Item(Game.weapons_sprite_sheet.getSubimage(246+43, 24, 43, 43), String.format("Sword %d", Game.random.nextInt(1000)),1, "a Sword", attack);
        return weapon;
    }

}
