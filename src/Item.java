import javax.swing.*;
import java.awt.*;

public class Item extends GameObject {

    protected Image image;
    protected String name;
    protected String description;
    public Spell attack;
    int amount;
    boolean consumable;
    Item(Image image, String name, int amount, String description, Spell attack, boolean consumable) {
        this.image = image;
        this.icon = new ImageIcon();
        this.icon.setImage(image.getScaledInstance(100, 100, 0));
        this.name = name;
        this.description = description;
        this.attack = attack;
        this.radius = 50;
        this.amount = amount;
        this.consumable = consumable;
    }

    static public Item random_weapon() {
        Spell attack;
        Image image;
        String name;
        if (Game.random.nextInt(2) == 0) {
            name = "Basic sword";
            image = Game.weapons_sprite_sheet.getSubimage(246 + 43, 24, 43, 43);
            attack = new Spell(new ImageIcon(Game.old_sprite_sheet.getSubimage(32 * 5, 32, 32, 32)), "Sword swing", Game.random.nextInt(20) + 5, 20, 10, 0, 80, 2, Spell.type_t.physical, null);
        } else {
            name = "Basic wand";
            image = Game.weapons_sprite_sheet.getSubimage(246 + 43 * 2, 24 + 43 * 9, 43, 43);
            attack = new Spell(new ImageIcon(Game.old_sprite_sheet.getSubimage(32, 0, 32, 32)), "Mana Bolt", Game.random.nextInt(20) + 5, 20, 50, 8, 20, 2, Spell.type_t.projectile, null);
        }
        Item weapon = new Item(image, name, 1, "A weapon", attack, false);
        return weapon;
    }

    static public Item random_spell_book() {
        Spell attack;
        Image image;
        String name;
        String description;
        int type = Game.random.nextInt(3);
        if (type ==0) {
            name = "Spell book";
            description = "A guide to cast Fireball";
            image = Game.wands_and_books_sprite_sheet.getSubimage(246, 24+43*5, 43, 43);
            attack = new Spell(new ImageIcon(Game.old_sprite_sheet.getSubimage(0, 0, 32, 32)), "fireball", Game.random.nextInt(10) + 10, 10, 50, 6, 30, 2, Spell.type_t.projectile, new Spell(new ImageIcon(Game.old_sprite_sheet.getSubimage(0, 0, 32, 32)), "Explosion", 20, 0, 10, 0, 100, 5, Spell.type_t.projectile, null));
        } else if (type ==1){
            name = "Spell book";
            description = "A guide to cast Mana Bolt";
            image = Game.wands_and_books_sprite_sheet.getSubimage(246+43*3, 24+43*5, 43, 43);
            attack = new Spell(new ImageIcon(Game.old_sprite_sheet.getSubimage(32, 0, 32, 32)), "Mana Bolt", Game.random.nextInt(10) + 5, 5, 50, 8, 20, 1, Spell.type_t.projectile, null);
        } else {
            name = "Spell scroll";
            description = "You can let mana flow through this to summon a Lava pool. consumable";
            image = Game.wands_and_books_sprite_sheet.getSubimage(246+43*5, 24+43*5, 43, 43);
            attack = new Spell(new ImageIcon("lavapool.png"), "Lava Pool", 10, 5, 200, 0, 100, 5, Spell.type_t.projectile, null);
        }
        Item weapon = new Item(image, name, 1, description, attack, false);
        return weapon;
    }
    public boolean equals(Item item) {

        if (this.name.equals(item.name) &&
                this.description.equals(item.description) &&
                ((this.attack == null && item.attack == null) ||
                        (this.attack.type == item.attack.type &&
                                this.attack.name.equals(item.attack.name) &&
                                this.attack.radius == item.attack.radius &&
                                this.attack.movement_speed == item.attack.movement_speed &&
                                this.attack.mana_cost == item.attack.mana_cost &&
                                this.attack.piercing == item.attack.piercing &&
                                this.attack.duration == item.attack.duration &&
                                this.attack.damage_health == item.attack.damage_health))) {
            return true;
        }
        return false;
    }
}
