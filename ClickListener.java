import java.awt.event.*;
import java.awt.Point;

public class ClickListener extends MouseAdapter{
    GamePanel gp;
    GameMechanism gm;

    ClickListener(GamePanel gp, GameMechanism gm) {
        this.gm = gm;
        this.gp = gp;
    }

    public void mousePressed(MouseEvent e) {
        gp.prevPt = e.getPoint();
        int x = (int)gp.prevPt.getX();
        int y = (int)gp.prevPt.getY();
        if(this.gp.gameState == 0) {
            getSelectedPet(x, y, gp.maxPetsNumber, gp.pet);
            getSelectedPet(x, y, gp.maxSelection, gp.petSelection);
        }
        getSelectedPet(x, y, gp.maxButtonNumber, gp.button);
    }

    private void getSelectedPet(int x, int y, int max, Entity[] p) {
        for(int i = 0; i < max; i++){ // mungkin collision detectionnya masih bisa dipindah ke dalam class lain
            if(p[i] == null) {
                continue;
            } else {
                if(x >= p[i].getX() && x <= p[i].getX() + p[i].getWidth()){
                    if(y >= p[i].getY() && y <= p[i].getY() + p[i].getHeight()){
                        if(max == gp.maxPetsNumber) {
                            gp.selectedPet = i;
                            // System.out.println("pet " + gp.selectedPet);
                        } else if (max == gp.maxSelection) {
                            gp.selectedSelection = i;
                            // System.out.println("selection " + gp.selectedSelection);
                        } else if (max == gp.maxButtonNumber) {
                            gp.selectedButton = i;
                            if(i == 0) rerollPet();
                            if(i == 1) {
                                if(gp.gameState ==  0) this.changeState();
                                else if(gp.gameState == 1){
                                    if(this.gp.gm.gameResult == 1) {
                                        this.gp.playerStatus.upWin();
                                        System.out.println(this.gp.playerStatus.getWin());
                                    } else if(this.gp.gm.gameResult == 2) {
                                        this.gp.playerStatus.donwLife();
                                        System.out.println(this.gp.playerStatus.getLife());
                                    }
                                    if(gm.gameResult >= 0 ) {
                                        gp.gameState--;
                                        gp.gm.resetPet();
                                        gp.gm.resetPanel();
                                        gp.playerStatus.resetCoin();
                                        gm.addEnemy();
                                        gm.updateEnemy();
                                        updatePlayerCoin();
                                        updatePlayerLife();
                                        updatePlayerWin();
                                        gm.gameTime = 0;
                                        gm.gameResult = -1;
                                    }
                                }
                            }
                            if(i == 2) {
                                System.out.println("retry");
                                this.resetAllPet();
                                updatePlayerCoin();
                                updatePlayerLife();
                                updatePlayerWin();
                            }
                        }
                        gp.isClicked = true;
                        // System.out.println(gp.isClicked);
                    }
                }
            }
        }
    }

    private void resetAllPet() {
        for(int i = 0; i < this.gp.maxPetsNumber; i ++) {
            if(this.gp.pet[i] != null) {
                this.gp.pet[i] = null;
            }
            if(this.gp.enemyPet[i] != null) {
                this.gp.enemyPet[i] = null;
            }
            this.gp.playerStatus.resetAll();
            this.resetSelection();
            this.resetEnemy();
        }
        
    }

    public void mouseReleased(MouseEvent e) {
        if(gp.selectedSelection >= 0)  {
            check(gp.maxPetsNumber, gp.selectedSelection, gp.petSelection);
            resetPosition(gp.selectedSelection, gp.setSelection, gp.petSelection);
        }
        if(gp.selectedPet >= 0) {
            check(gp.maxPetsNumber, gp.selectedPet, gp.pet);
            resetPosition(gp.selectedPet, gp.setTeam, gp.pet);
            // System.out.println("pet reset");
        }
        resetValue();
    }

    public void resetSelection() {
        String fileName;
        for(int i = 0; i < this.gp.maxSelection;  i++) {
            fileName = this.gp.petName.getRandomName();
            if(this.gp.petSelection[i] != null) {
                this.gp.petSelection[i] = null;
            }
            this.gp.petSelection[i] = new Pet((int)(this.gp.setSelection[i].getX()), (int)(this.gp.setSelection[i].getY()), fileName, this.gp);
        }
    }

    public void resetEnemy() {
        String fileName;
        for(int i = 0; i < 3; i ++) {
            fileName = this.gp.petName.getRandomName();
            this.gp.enemyPet[i] = new Pet((int)(this.gp.setEnemyBattle[i].getX()), (int)(this.gp.setEnemyBattle[i].getY()), fileName, this.gp);
        }
    }
    
    private void resetPosition(int index, Point[] origin, Pet[] p) {
        if(gp.isClicked == true) {
            Point resetPoint = origin[index];
            int dx = (int)(resetPoint.getX() - p[index].getX() );
            int dy = (int)(resetPoint.getY() - p[index].getY() );
            p[index].getPoint().translate(dx, dy);
            if(gp.collide >= 0) {
                // increasePetCount();
                // gp.petSelection[index].getPoint().translate(dx, dy);
                if(gp.selectedSelection >= 0 && gp.playerStatus.getCoin() >= 3) {
                    buyPet(index, p);
                }
                else if(gp.selectedPet >= 0) {
                    movePet(index, p);
                }
            }
        }
    }

    private void movePet(int index, Pet[] p) {
        if(gp.pet[gp.collide] == null) {
            instantiatePet(index, p);
        } else if(gp.collide == gp.selectedPet) {
            String name = gp.pet[gp.selectedPet].getFileInput();
            int level = gp.pet[gp.selectedPet].getLevel();
            gp.pet[gp.selectedPet] = null;
            gp.pet[gp.collide] =  new Pet((int)(this.gp.setTeam[gp.collide].getX()), (int)(this.gp.setTeam[gp.collide].getY()), name, this.gp);
            for(int i = 1; i < level; i ++) {
                gp.pet[gp.collide].upLevel();
            }
        } else if (gp.collide != gp.selectedPet && gp.pet[gp.collide] != null && gp.pet[gp.selectedPet].getFileInput().equals(gp.pet[gp.collide].getFileInput())) {
            gp.pet[gp.selectedPet] = null;
            gp.pet[gp.collide].upLevel();
        } else if(gp.pet[gp.collide] != null) {
            String a = gp.pet[gp.selectedPet].getFileInput();
            int a_level = gp.pet[gp.selectedPet].getLevel();
            String b = gp.pet[gp.collide].getFileInput();
            int b_level = gp.pet[gp.collide].getLevel();
            changePet(gp.selectedPet, b);
            changePet(gp.collide, a);
            updateLevelChange(b_level, gp.pet[gp.selectedPet]);
            updateLevelChange(a_level, gp.pet[gp.collide]);
        }
    }
    private void changePet(int index, String file) {
        gp.pet[index] = null;
        gp.pet[index] = new Pet((int)(gp.setTeam[index].getX()), (int)(gp.setTeam[index].getY()), file, gp);
    }

    private void updateLevelChange(int level, Pet p) {
        for (int i = 1; i < level; i ++) {
            p.upLevel();
        }
    }

    private void buyPet(int index, Pet[] p) {
        int cost = -3;
        if(gp.pet[gp.collide] == null) {
            gp.playerStatus.setCoin(cost);
            instantiatePet(index, p);
        } else if(gp.pet[gp.collide] != null && gp.petSelection[gp.selectedSelection].getFileInput().equals(gp.pet[gp.collide].getFileInput())){ 
            gp.playerStatus.setCoin(cost);
            gp.petSelection[gp.selectedSelection] = null;
            gp.pet[gp.collide].upLevel();
        }
        updatePlayerCoin();
    }

    private void instantiatePet(int index, Pet[] p) {
        // System.out.println(gp.collide);
        if(gp.pet[gp.collide] == null ) {
            int lvl = p[index].getLevel();
            int x = (int)(gp.setTeam[gp.collide].getX());
            int y = (int)(gp.setTeam[gp.collide].getY());
            String name = p[index].getFileInput();
            System.out.println("pet[" + gp.collide + "] created");
            gp.pet[gp.collide] = new Pet(x, y, name, gp);
            for(int i = 1; i < lvl; i ++) {
                // System.out.println("upLve");
                gp.pet[gp.collide].upLevel();
            }
            p[index] = null;
        } 
    }

    private void check(int max, int index, Pet[] p) {
        for(int j = 0; j < max; j++){
            if(p[index].getCollision(gp.setTeam[j]) == true) {
                gp.collide = j;
                // System.out.println("collide " + gp.collide);
            }
        }
    }
    
    private void resetValue() {
        gp.isClicked = false;
        gp.selectedPet = -1;
        gp.selectedSelection = -1;
        gp.collide = -1;
        gp.selectedButton = -1;
    }

    private void rerollPet() {
        int cost = -3;
        if(gp.playerStatus.getCoin() >= 3) {
            gp.playerStatus.setCoin(cost);
            for(int i = 0; i < gp.maxSelection; i++) {
                gp.petSelection[i] = null;
                gp.petSelection[i] = gp.petSelection[i] = new Pet((int)(gp.setSelection[i].getX()), (int)(gp.setSelection[i].getY()), gp.petName.getRandomName() ,gp);
            }
        }
        updatePlayerCoin();
    }

    private void updatePlayerCoin() {
        gp.status[0] = null;
        String newFileName = "coin" + gp.playerStatus.getCoin();
        gp.status[0] = new Button((int)(gp.setStatus[0].getX()), (int)(gp.setStatus[0].getY()), newFileName, gp);
    }

    private void updatePlayerLife() {
        gp.status[1] = null;
        String newLife = "life" + gp.playerStatus.getLife();
        gp.status[1] = new Button((int)(gp.setStatus[1].getX()), (int)(gp.setStatus[1].getY()), newLife, gp);
    }

    private void updatePlayerWin() {
        gp.status[2] = null;
        String newWin = "win" + gp.playerStatus.getWin();
        gp.status[2] = new Button((int)(gp.setStatus[2].getX()), (int)(gp.setStatus[2].getY()), newWin, gp);
    }

    private void changeState() {
        if(this.gp.playerStatus.getWin() < 3 && this.gp.playerStatus.getLife() > 0) {
            gp.gameState++;
            for(int i = 0; i < gp.maxPetsNumber; i++) {
                if(gp.pet[i] != null) {
                    Point resetPoint = gp.pet[i].getPoint();
                    int dx = (int)(gp.setPlayerBattle[i].getX() - resetPoint.getX());
                    int dy = (int)(gp.setPlayerBattle[i].getY() - resetPoint.getY());
                    gp.pet[i].getPoint().translate(dx, dy);
                    gp.pet[i].changeStatusLocation();
                    gp.pet[i].changeLevelLocation();
                }
            }
        }
        // if(gp.gameState == 1) gp.gameState --;

    }
}
