import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    private final int tileSize = 120;
    private final int maxCol = 16;
    private final int maxRow = 9;

    private final int screenWidth = tileSize * maxCol;
    private final int screenHeight = tileSize * maxRow;

    private Thread gameThread;
    private KeyHandler keyH = new KeyHandler();

    // Player Location (delete)
    // private int playerX = 100;
    // private int playerY = 100;
    // private int playerSpeed = 10;

    // FPS
    private int FPS = 60;

    // Backgroud Image
    private Image backgroundImage;

    // Point
    Point prevPt;
    Point[] setTeam;
    Point[] setSelection;

    final int setTeamX = 370;
    final int setTeamY = 540;
    final int maxPetsNumber = 5;
    // public int actualPetsNumber = 0;
    final int selectionX = 370;
    final int selectionY = 770;
    final int maxSelection = 3;

    // Checkin clicked
    public boolean isClicked;

    // test
    Pet[] pet; // belum instantiate
    Pet[] petSelection;
    Stone[] petStone;
    Stone[] selectionStone;
    public int selectedPet = -1;
    public int selectedSelection = -1;
    public int collide = -1;

    // PetName
    PetName petName;
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);

        this.backgroundImage = new ImageIcon("D:\\Kosen\\Java\\PRumble\\res\\setup.png").getImage();

        // set pet
        ClickListener clickListener = new ClickListener(this);
        DragListener dragListener = new DragListener(this);
        this.isClicked = false;

        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);

        // Point
        this.setTeam = new Point[this.maxPetsNumber];
        this.petStone = new Stone[this.maxPetsNumber];
        for(int i = 0; i < this.maxPetsNumber; i ++) {
            this.setTeam[i] = new Point(this.setTeamX + 180*i, this.setTeamY);
            this.petStone[i] = new Stone((int)(this.setTeam[i].getX()) - 10, (int)(this.setTeam[i].getY()) + 75, "stone", this);
        }

        this.setSelection = new Point[this.maxSelection];
        this.petSelection = new Pet[this.maxSelection];
        this.selectionStone = new Stone[this.maxSelection];
        for(int i = 0; i < this.maxSelection; i ++) {
            this.setSelection[i] = new Point(this.selectionX + 180*i, this.selectionY);
            this.petSelection[i] = new Pet((int)(setSelection[i].getX()), (int)(setSelection[i].getY()), "chicken" ,this);
            this.selectionStone[i] = new Stone((int)(setSelection[i].getX())-10, (int)(setSelection[i].getY())+75, "stone", this);
        }
        
        // this.petSelection[2] = null;

        this.pet = new Pet[this.maxPetsNumber];

        this.petName = new PetName();
    }

    public void startGameThread() {
        this.gameThread = new Thread(this); // passing runnable ke sini
        this.gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/this.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        // long drawCount = 0;

        while(this.gameThread != null) {
            // System.out.println("running");

            // GAME LOOP
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime  = currentTime;

            if(delta >= 1) {
                // UPDATE
                update();

                // REDRAW
                repaint();


                delta --;
                // drawCount++;
            }

            if(timer >= 1000000000) {

                System.out.println(petName.getRandomName());
            //     System.out.println("FPS : " + drawCount);
            //     drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        // if(this.keyH.upPressed == true) {
        //     this.playerY -= this.playerSpeed;
        // }
        // if(this.keyH.downPressed == true) {
        //     this.playerY += this.playerSpeed;
        // }
        // if(this.keyH.leftPressed == true) {
        //     this.playerX -= this.playerSpeed;
        // }
        // if(this.keyH.rightPressed == true) {
        //     this.playerX += this.playerSpeed;
        // }
    }

    // DRAWING OBJECT
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(backgroundImage, 0, 0, null);
        for(int i = 0; i < this.maxPetsNumber; i ++) {
            this.petStone[i].draw(g2);
        }
        for(int i = 0; i < this.maxSelection; i++) {
            this.selectionStone[i].draw(g2);
            if(this.petSelection[i] != null) {
                this.petSelection[i].draw(g2);
            }
        }
        for(int i = 0; i < this.maxPetsNumber; i++) {
            if(this.pet[i] != null){
                this.pet[i].draw(g2);
            }
        }


        g2.setColor(Color.WHITE);
        // g2.fillRect(playerX, playerY, 120, 120);
        g2.dispose();
    }
}