import java.awt.*;
import javax.swing.ImageIcon;

public class Pet extends Entity {
    private int attack;
    private int health;
    private int level;
 
    private ImageIcon statusIcon;
    private Point setStatus;
 
    private ImageIcon levelIcon;
    private Point setLevel;

    public Pet(int x, int y, String image, GamePanel gp) {
        super(x, y, image, gp);
        this.level = 1;
        setPetStatus();
        this.statusIcon = new ImageIcon("res/status.png");
        this.levelIcon = new ImageIcon("res/lvl1.png");
        this.setStatus = new Point(super.getX(), super.getY()+140);
        this.setLevel = new Point(super.getX(), super.getY()-30);
    }

    private void setPetStatus() {
        if(super.getFileInput() == "bird") {
            this.setAttack(2);
            this.setHealth(1);
        }
        if(super.getFileInput() == "boar") {
            this.setAttack(2);
            this.setHealth(2);
        }
        if(super.getFileInput() == "cat") {
            this.setAttack(1);
            this.setHealth(2);
        }
        if(super.getFileInput() == "chicken") {
            this.setAttack(1);
            this.setHealth(3);
        }
        if(super.getFileInput() == "crab") {
            this.setAttack(2);
            this.setHealth(3);
        }
        if(super.getFileInput() == "croc") {
            this.setAttack(2);
            this.setHealth(1);
        }
        if(super.getFileInput() == "shark") {
            this.setAttack(2);
            this.setHealth(2);
        }
        if(super.getFileInput() == "shrimp") {
            this.setAttack(1);
            this.setHealth(2);
        }
        if(super.getFileInput() == "sloth") {
            this.setAttack(3);
            this.setHealth(1);
        }
        if(super.getFileInput() == "squid") {
            this.setAttack(1);
            this.setHealth(1);
        }
    }

    private void setAttack(int att) {
        this.attack = att;
    }
    
    private void setHealth(int hp) {
        this.health = hp;
    }

    public void upLevel() {
        this.level ++;
        this.attack ++;
        this.health ++;
        updateLevel();
    }

    private void updateLevel() {
        String filename = "lvl" + Integer.toString(getLevel());
        this.levelIcon = null;
        this.levelIcon = new ImageIcon("res/" + filename + ".png");
    }
    
    public int getAttack() {
        return this.attack;
    }

    public int getHealth() {
        return this.health;
    }

    public int getLevel() {
        return this.level;
    }
    
    public ImageIcon getStatusIcon() {
        return this.statusIcon;
    }

    private ImageIcon getLevelIcon() {
        return this.levelIcon;
    }

    public void draw(Graphics g2) {
        super.draw(g2);
        drawStatus(g2);
        drawLevel(g2);
    }

    private void drawStatus(Graphics g2) {
        this.getStatusIcon().paintIcon(super.getGP(), g2, (int)(this.setStatus.getX()), (int)(this.setStatus.getY()));
        g2.setFont(new Font("SUPER GAME", Font.PLAIN, 30));
        g2.setColor(Color.BLACK);
        g2.drawString(Integer.toString(this.getAttack()), (int)(this.setStatus.getX()) + 47, (int)(this.setStatus.getY()) + 34);
        g2.drawString(Integer.toString(this.getHealth()), (int)(this.setStatus.getX()) + 104, (int)(this.setStatus.getY()) + 34);
    }

    private void drawLevel(Graphics g2) {
        this.getLevelIcon().paintIcon(super.getGP(), g2, (int)(this.setLevel.getX()), (int)(this.setLevel.getY()));
    }

    public void changeStatusLocation() {
        // System.out.println(super.getX());
        // System.out.println(super.getY());
        this.setStatus = null;
        this.setStatus = new Point(super.getX(), super.getY()+140);
    }

    public void changeLevelLocation() {
        this.setLevel = null;
        this.setLevel = new Point(super.getX(), super.getY()-30);
    }

    public void isAttacked(int att) {
        this.health -= att;
    }
}