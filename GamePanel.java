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

    public int gameState = 0;
    // Player Location (delete)
    // private int playerX = 100;
    // private int playerY = 100;
    // private int playerSpeed = 10;

    // FPS
    private int FPS = 60;

    // Backgroud Image
    public Image setBackgroundImage;
    public Image battleBackgroundImage;

    // Point
    Point prevPt;
    Point[] setTeam;
    Point[] setSelection;
    Point[] setButton;
    Point[] setStatus;

    final int setTeamX = 370;
    final int setTeamY = 540;
    final int selectionX = 370;
    final int selectionY = 770;
    final int statusX = 30;
    final int statusY = 30;

    final int maxPetsNumber = 5;
    final int maxButtonNumber = 4;
    final int maxStatus = 3;
    final int maxSelection = 3;

    // Checkin clicked
    public boolean isClicked;
    public boolean reset = true;

    // test
    Pet[] pet;
    Pet[] petSelection;
    Stone[] petStone;
    Stone[] selectionStone;
    public int selectedPet = -1;
    public int selectedSelection = -1;
    public int selectedButton = -1;
    public int collide = -1;

    // PetName
    PetName petName;

    // Button
    Button[] button;
    Button[] status;
    Button[] showResult;

    // Player
    Player playerStatus;

    // Battle
    Point[] setPlayerBattle;
    Point[] setEnemyBattle;

    Pet[] enemyPet;

    // Game mechanism
    public GameMechanism gm;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);

        this.setBackgroundImage = new ImageIcon("res/setup.png").getImage();
        this.battleBackgroundImage = new ImageIcon("res/battle.png").getImage();

        // Initiate status
        this.playerStatus = new Player();

        this.petName = new PetName();

        this.setButton = new Point[this.maxButtonNumber];
        this.setButton[0] = new Point(0 + 30, screenHeight - 130);
        this.setButton[1] = new Point(screenWidth - 40 - 380, screenHeight - 130);

        
        this.button = new Button[this.maxButtonNumber];
        this.button[0] = new Button((int)(setButton[0].getX()), (int)(setButton[0].getY()) , "reroll", this);
        this.button[1] = new Button((int)(setButton[1].getX()), (int)(setButton[1].getY()) , "endturn", this);
        this.button[2] = new Button(this.screenWidth-130, this.screenHeight- 130, "retry", this);
        
        this.showResult = new Button[5];
        this.showResult[0] = new Button(this.screenWidth/2 - 250, this.screenHeight/2-125, "result1", this);
        this.showResult[1] = new Button(this.screenWidth/2 - 250, this.screenHeight/2-125, "result2", this);
        this.showResult[2] = new Button(this.screenWidth/2 - 250, this.screenHeight/2-125, "round1", this);
        this.showResult[3] = new Button(this.screenWidth/2 - 250, this.screenHeight/2-125, "round2", this);
        this.showResult[4] = new Button(this.screenWidth/2 - 250, this.screenHeight/2-125, "round3", this);

        this.setStatus = new Point[this.maxStatus];
        for(int i = 0; i < this.maxSelection; i ++) {
            this.setStatus[i] = new Point(statusX + 150*i, statusY);
        }

        this.status = new Button[this.maxStatus];
        this.status[0] = new Button((int)(setStatus[0].getX()), (int)(setStatus[0].getY()), "coin10", this);
        this.status[1] = new Button((int)(setStatus[1].getX()), (int)(setStatus[1].getY()), "life3", this);
        this.status[2] = new Button((int)(setStatus[2].getX()), (int)(setStatus[2].getY()), "win0", this);
        

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
            this.petSelection[i] = new Pet((int)(setSelection[i].getX()), (int)(setSelection[i].getY()), petName.getRandomName(),this);
            this.selectionStone[i] = new Stone((int)(setSelection[i].getX())-10, (int)(setSelection[i].getY())+75, "stone", this);
        }

        
        
        this.pet = new Pet[this.maxPetsNumber];
        
        // Battle
        this.setPlayerBattle = new Point[this.maxPetsNumber];
        for(int i = 0; i < this.maxPetsNumber; i++) {
            this.setPlayerBattle[i] = new Point(this.setTeamX - 340 + 180*i, this.selectionY);
        }
        this.setEnemyBattle = new Point[this.maxPetsNumber];
        this.enemyPet = new Pet[this.maxPetsNumber];
        for(int i = this.maxPetsNumber - 1 ; i >= 0; i --) {
            this.setEnemyBattle[i] = new Point(this.screenWidth/2 + 50 + 180*i, this.selectionY);
            if(i<=2) this.enemyPet[i] = new Pet((int)(setEnemyBattle[i].getX()), (int)(setEnemyBattle[i].getY()), petName.getRandomName(), this);
        }

        this.gm = new GameMechanism(pet, enemyPet, this);

        // set pet
        ClickListener clickListener = new ClickListener(this, this.gm);
        DragListener dragListener = new DragListener(this);
        this.isClicked = false;

        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
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
                // System.out.println("state " + this.gameState);
                if(this.gameState == 1) {
                    gm.gameTime += timer;
                    // battleStart();
                    gm.gameStart();
                    if(gm.gameTime >= 2000000000) {
                        gm.gameTime = 0;
                    }
                } else {
                    if(this.playerStatus.getLife() <= 0) {
                        System.out.println("You Lose");
                    } else if(this.playerStatus.getWin() >= 3) {
                        System.out.println("PLayer WIN");
                    }
                }
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
        // draw bg
        if(this.gameState == 0) {
            g2.drawImage(this.setBackgroundImage, 0, 0, null);
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
            for(int i = 0; i < this.maxButtonNumber ; i++) {
                if(this.button[i] != null) {
                    this.button[i].draw(g2);
                }
            }
            for(int i = 0; i < this.maxStatus; i ++) {
                if(this.status[i] != null) {
                    this.status[i].draw(g2);
                }
            }
        } else if(this.gameState == 1) {
            g2.drawImage(this.battleBackgroundImage, 0, 0, null);
            for(int i = 0; i < this.maxPetsNumber; i++) {
                if(this.pet[i] != null){
                    this.pet[i].draw(g2);
                }
                if(this.enemyPet[i] != null) {
                    this.enemyPet[i].draw(g2);
                }
            }
        }
        this.button[1].draw(g2);
        this.button[2].draw(g2);
        if(this.playerStatus.getLife() == 0) {
            this.showResult[1].draw(g2); 
        } else if (this.playerStatus.getWin() == 3) {
            this.showResult[0].draw(g2); 
        }
        if(this.gameState == 1) {
            if(this.gm.gameResult == 0) {
                this.showResult[4].draw(g2);
            } else if(this.gm.gameResult == 1) {
                this.showResult[2].draw(g2);
            }else if(this.gm.gameResult == 2) {
                this.showResult[3].draw(g2);
            }
        }

        g2.dispose();
    }

}