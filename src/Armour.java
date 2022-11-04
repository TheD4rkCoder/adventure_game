import java.awt.*;

public class Armour extends Item{
    protected double hpBuff;
    protected double defenceBuff;
    Armour(Image image, String name, String description, double hpBuff, double defenceBuff, Spell attack) {
        super(image, name, 1, description, attack);
        this.hpBuff = hpBuff;
        this.defenceBuff = defenceBuff;
    }
}
