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
        getSelected(x, y, gp.maxPetsNumber, gp.pet);
        getSelected(x, y, gp.maxSelection, gp.petSelection);
    }

    private void getSelected(int x, int y, int max, Pet[] p) {
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
                instantiatePet(index, p);
            }
        }
    }

    private void instantiatePet(int index, Pet[] p) {
        // System.out.println(gp.collide);
        if(gp.pet[gp.collide] == null) {
            int x = (int)(gp.setTeam[gp.collide].getX());
            int y = (int)(gp.setTeam[gp.collide].getY());
            String name = p[index].getFileInput();
            System.out.println("pet[" + gp.collide + "] created");
            gp.pet[gp.collide] = new Pet(x, y, name, gp);
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
    }

    // public void increasePetCount() {
    //     if(gp.pet[gp.collide] == null && gp.selectedPet >= 0) {
    //         // gp.actualPetsNumber ++;
    //     }
    //     // System.out.println(gp.actualPetsNumber);
    // }
}
