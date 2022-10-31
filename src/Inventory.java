import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Inventory extends JPanel implements MouseInputListener{
        protected int x, y;

        protected Item[] items;
        protected Item[] hotBar;

        protected boolean opened;

        protected Area[] levelUps;

        protected Area[] itemSelection;
        protected int selectedItem;

        protected Area[] hotBarSelection;

        Inventory(){
                this.setPreferredSize(new Dimension(1280,720));
                this.x = 200;
                this.y = 200;

                this.opened = false;

                items = new Item[9];
                for(int i = 0; i < 9; ++i){
                        items[i] = new Item(new ImageIcon("img.png").getImage(), "Test", "Test");
                }
                hotBar = new Item[3];
                for(int i = 0; i < 3; ++i){
                        hotBar[i] = new Item(new ImageIcon("img.png").getImage(), "Test", "Test");
                }

                addItem(new Item(new ImageIcon("img.png").getImage(), "Oha", "Test"));
                addItem(new Item(new ImageIcon("img.png").getImage(), "Oha", "Test"));

                levelUps = new Area[5];

                levelUps[0] = new Area(this.x + 596, this.y, 20, 20); //Intelligence
                levelUps[1] = new Area(this.x + 596, this.y + 65, 20, 20); //Strength
                levelUps[2] = new Area(this.x + 596, this.y + 145, 20, 20); //Endurance
                levelUps[3] = new Area(this.x + 596, this.y + 180, 20, 20); //Dexterity
                levelUps[4] = new Area(this.x + 596, this.y + 245, 20, 20); //Wisdom

                selectedItem = 13;
                itemSelection = new Area[9];
                for(int i = 0; i < 3; ++i){
                        for(int j = 0; j < 3; ++j){
                                itemSelection[i * 3 + j] = new Area(this.x + j * 110, this.y + i * 110, 100, 100);
                        }
                }

                hotBarSelection = new Area[3];
                for(int i = 0; i < 3; ++i){
                        hotBarSelection[i] = new Area(this.x + 110 * i, this.y + 370, 100, 100);
                }

        }

        public void dropItem(Item item){
                for(int i = 0; i < 9; ++i){
                        if(i < 3){
                                if(hotBar[i].name.equals(item.name)){
                                        hotBar[i] = new Item(new ImageIcon("img.png").getImage(), "Test", "Test");
                                }
                        }
                        if(items[i].name.equals(item.name)){
                                items[i] = new Item(new ImageIcon("img.png").getImage(), "Test", "Test");
                        }
                }
                selectedItem = 13;
        }

        public void addItem(Item item){
                for(int i = 0; i < 9; ++i){
                        if(items[i].name.equals("Test")){
                                items[i] = item;
                                /* //Adds Item to HotBar as well
                                for(i = 0; i < 3; ++i){
                                        if(hotBar[i].name.equals("Test")){
                                                hotBar[i] = item;
                                                break;
                                        }
                                }
                                */

                                break;
                        }
                }
        }

        public void paint (Graphics g){ //Due to the transferring of the graphics object from GameFrame the position gets offset so that 0 0 in GamePanel are 7 and 30 here
                Graphics2D g2D = (Graphics2D) g;
                int windowWidth = (int)Math.round(g.getClip().getBounds().getWidth());
                int windowHeight = (int)Math.round(g.getClip().getBounds().getHeight());

                //ItemArea
                g2D.setPaint(Color.BLACK);
                float alpha = 6 * 0.1f;
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

                if(!opened) {
                        g2D.setComposite(alcom);
                        g2D.fillRect(0, 0, windowWidth, windowHeight);

                        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
                        g2D.setComposite(alcom);
                }

                //Draw Inventory outline/background
                g2D.fillRect(190,190,920, 350);


                for(int i = 0; i < 3; ++i){
                        for(int j = 0; j < 3; ++j){
                                if(items[i*3 + j].name.equals("Test")) {
                                        g2D.setPaint(Color.GRAY);
                                        g2D.fillRect(this.x + j * 110, this.y + i * 110, 100, 100);
                                }else{
                                        g2D.drawImage(items[i * 3 + j].icon.getImage(),  this.x + j * 110,  this.y + i * 110, null);
                                }
                        }
                }

                int xDist = this.x + 330;

                //Insert Player image
                g2D.drawImage(Game.player.image.getImage(), xDist, this.y, null);

                Font attributes = new Font("Arial", Font.PLAIN, 20);
                Font specifics = new Font("Arial", Font.PLAIN, 15);


                //BaseStats I guess

                g2D.setFont(specifics);
                g2D.setPaint(Color.red);
                g2D.drawString("HP: " + (int)Game.player.hp + "/" + (int)Game.player.maxHP, xDist,  this.y + 271);
                g2D.setPaint(Color.green);
                g2D.drawString("Defence: " + Game.player.def, xDist,  this.y + 286);
                g2D.setPaint(Color.orange);
                g2D.drawString("Stamina: " + Game.player.stamina + "/" + Game.player.maxStamina, xDist,  this.y + 301);
                g2D.setPaint(Color.blue);
                g2D.drawString("Mana: " + (int) Game.player.mana + "/" + (int) Game.player.maxMana, xDist,  this.y + 316);

                xDist += 266;
                //LevelUpButtons
                if(Game.player.pointsAvailable < 1){
                        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                        g2D.setComposite(alcom);
                }
                g2D.setPaint(Color.cyan);
                g2D.fillRect(this.levelUps[0].x, this.levelUps[0].y, this.levelUps[0].width, this.levelUps[0].height);
                g2D.fillRect(this.levelUps[1].x, this.levelUps[1].y, this.levelUps[1].width, this.levelUps[1].height);
                g2D.fillRect(this.levelUps[2].x, this.levelUps[2].y, this.levelUps[2].width, this.levelUps[2].height);
                g2D.fillRect(this.levelUps[3].x, this.levelUps[3].y, this.levelUps[3].width, this.levelUps[3].height);
                g2D.fillRect(this.levelUps[4].x, this.levelUps[4].y, this.levelUps[4].width, this.levelUps[4].height);

                if(Game.player.pointsAvailable < 1){
                        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
                        g2D.setComposite(alcom);
                }

                xDist += 25;
                //Attributes
                g2D.setFont(attributes);
                g2D.setPaint(Color.decode("0x0032CEF3"));
                g2D.drawString("Intelligence: " + Game.player.intelligence, xDist,  this.y + attributes.getSize());
                g2D.drawString("Strength: " + Game.player.strength, xDist,  this.y + attributes.getSize()*2 + specifics.getSize()*3);
                g2D.drawString("Endurance: " + Game.player.endurance, xDist,  this.y + attributes.getSize()*3 + specifics.getSize()*7);
                g2D.drawString("Dexterity: " + Game.player.dexterity, xDist,  this.y + attributes.getSize()*4 + specifics.getSize()*8);
                g2D.drawString("Wisdom: " + Game.player.wisdom, xDist,  this.y + attributes.getSize()*5 + specifics.getSize()*11);

                //Specific Stats
                xDist += 20;
                g2D.setFont(specifics);
                g2D.setPaint(Color.decode("0x0025A1BE"));
                //Intelligence
                g2D.drawString("Mana Recovery Speed (per Tick): " + String.format("%.2f", Game.player.mana_recovery_speed), xDist, this.y + attributes.getSize() + specifics.getSize());
                g2D.drawString("Spell Effectiveness: " + String.format("%.2f", Game.player.spell_effectiveness) + "x", xDist, this.y + attributes.getSize() + specifics.getSize()*2);
                g2D.drawString("Boosts: Mana", xDist, this.y + attributes.getSize() + specifics.getSize()*3);
                //Strength
                g2D.drawString("Base Damage: " + String.format("%.2f", Game.player.baseDamage), xDist, this.y + attributes.getSize()*2 + specifics.getSize()*4);
                g2D.drawString("Melee Damage Multiplier: " + String.format("%.2f", Game.player.melee_dmg_multiplier)+ "x", xDist, this.y + attributes.getSize()*2 + specifics.getSize()*5);
                g2D.drawString("Critical Hit Damage Multiplier: " + String.format("%.2f", Game.player.crit_dmg_multiplier)+ "x", xDist, this.y + attributes.getSize()*2 + specifics.getSize()*6);
                g2D.drawString("Boosts: HP, Defence", xDist, this.y + attributes.getSize()*2 + specifics.getSize()*7);
                //Endurance
                g2D.drawString("Boosts: HP, Defence, Stamina", xDist, this.y + attributes.getSize()*3 + specifics.getSize()*8);
                //Dexterity
                g2D.drawString("Movement Speed: " + String.format("%.2f", Game.player.movement_speed), xDist, this.y + attributes.getSize()*4 + specifics.getSize()*9);
                g2D.drawString("Combo Damage Multiplier: " + String.format("%.2f", Game.player.combo_dmg_multiplier)+ "x", xDist, this.y + attributes.getSize()*4 + specifics.getSize()*10);
                int critStage = 0;
                for(int i = 0; i < 8; ++i){
                        if(Game.player.critical_stage[i]){
                                ++critStage;
                        }
                }
                g2D.drawString("Critical Stage: " + critStage, xDist, this.y + attributes.getSize()*4 + specifics.getSize()*11);
                //Wisdom
                g2D.drawString("Item Stat Multiplier: " + String.format("%.2f", Game.player.item_stat_multiplier)+ "x", xDist, this.y + attributes.getSize()*5 + specifics.getSize()*12);
                g2D.drawString("Mastery Multiplier: " + String.format("%.2f", Game.player.mastery_multiplier)+ "x", xDist, this.y + attributes.getSize()*5 + specifics.getSize()*13);
                g2D.drawString("Comprehension Speed: " + String.format("%.2f", Game.player.comprehension_speed), xDist, this.y + attributes.getSize()*5 + specifics.getSize()*14);

                //
                g2D.setFont(attributes);
                g2D.setPaint(Color.decode("0x000F00FF"));
                g2D.drawString("Points available: " + Game.player.pointsAvailable, this.x + 330 + 266, this.y + attributes.getSize()*5 + specifics.getSize()*15 + 5);

                //HotBar 360
                g2D.setPaint(Color.BLACK);
                g2D.fillRect(this.x, this.y + 360, 340, 120);
                for(int i = 0; i < 3; ++i){
                        if(hotBar[i].name.equals("Test")) {
                                g2D.setPaint(Color.GRAY);
                                g2D.fillRect(this.x + 10 + i * 110, this.y + 370, 100, 100);
                        }else{
                                g2D.drawImage(items[i].icon.getImage(),  this.x  + 10 + i * 110,  this.y + 370, null);
                        }
                }



        }


        public void open (){
                this.opened = true;
        }

        public void close(){
                this.opened = false;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {

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

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

}
