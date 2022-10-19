import javax.swing.ImageIcon;
import java.awt.*;

public class Entity{ // ini masih ada kemungkinan untuk jadi parent class gitu
    
    private ImageIcon Image;
    private GamePanel gp;
    Point imageCorner;
    Point center;
    private String fileInput;
    
    public Entity (int x, int y, String image ,GamePanel gp) {
        this.fileInput = image;
        String filename = "res\\" + image + ".png";
        this.gp = gp;
        imageCorner = new Point(x, y);
        this.Image = new ImageIcon(filename);
    }

    public Entity(String name, int hp, int attack, int level, String element) {
    }

    public Point getPoint() {
        return this.imageCorner;
    }

    public int getX() {
        return (int)getPoint().getX();
    }

    public int getY() {
        return (int)getPoint().getY();
    }

    public int getWidth() {
        return Image.getIconWidth();
    }

    public int getHeight() {
        return Image.getIconHeight();
    }

    public String getFileInput() {
        return this.fileInput;
    }
    public Point getCenter() {
        int centerX = (int)this.imageCorner.getX() + this.getWidth()/2;
        int centerY = (int)this.imageCorner.getY() + this.getHeight()/2;

        this.center = new Point(centerX, centerY);
        return this.center;
    }
    
    public boolean getCollision(Point p) {
        double pX = p.getX()+this.getWidth()/2;
        double pY = p.getY()+this.getHeight()/2;
        double distance = Math.sqrt((this.getCenter().getX() - pX)*(this.getCenter().getX() - pX) + (this.getCenter().getY() - pY)*(this.getCenter().getY() - pY));
        boolean collision = false;
        // System.out.println("Distance : " + (int)distance);
        // System.out.println(pX);
        // System.out.println(pY);
        if((int)distance <= 70) collision = true;
        else collision = false;
        return collision;
    }
    public void draw(Graphics g2) {
        this.Image.paintIcon(this.gp, g2, (int)this.imageCorner.getX(), (int)this.imageCorner.getY());
    }
}
