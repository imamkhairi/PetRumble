import java.awt.event.*;
import java.awt.Point;

public class ClickListener extends MouseAdapter{
    GamePanel gp;
    
    ClickListener(GamePanel gp) {
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
                                if(gp.gameState == 0) this.changeState();
                                else if(gp.gameState == 1){
                                    gp.gameState--;
                                    gp.gm.resetPet();
                                    gp.gm.resetPanel();
                                    gp.playerStatus.resetCoin();
                                    updatePlayerCoin();
                                }
                            }
                        }
                        gp.isClicked = true;
                        // System.out.println(gp.isClicked);
                    }
                }
            }
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
        } else if (gp.collide != gp.selectedPet && gp.pet[gp.collide] != null && gp.pet[gp.selectedPet].getFileInput().equals(gp.pet[gp.collide].getFileInput())) {
            gp.pet[gp.selectedPet] = null;
            gp.pet[gp.collide].upLevel();
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

    private void changeState() {
        if(gp.gameState == 0) {
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
