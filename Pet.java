
public class Pet extends Entity {
    private int attack;
    private int health;

    public Pet(int x, int y, String image, GamePanel gp) {
        super(x, y, image, gp);
        this.attack = 2;
        this.health = 2;
        //TODO Auto-generated constructor stub
    }

    public int getAttack() {
        return this.attack;
    }

    public int getHealth() {
        return this.health;
    }
}