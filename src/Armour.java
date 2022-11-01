import java.awt.*;

public class Armour extends Item{
    protected double hpBuff;
    protected double defenceBuff;
    Armour(Image image, String name, String description, double hpBuff, double defenceBuff) {
        super(image, name, description);
        this.hpBuff = hpBuff;
        this.defenceBuff = defenceBuff;
    }
}
