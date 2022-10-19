import java.awt.event.*;
import java.awt.Point;

public class DragListener extends MouseMotionAdapter{
    GamePanel gp;

    public DragListener(GamePanel gp) {
        this.gp = gp;
    }

    public void mouseDragged(MouseEvent e) {
        if(gp.isClicked == true) {
            for(int i = 0; i < gp.maxPetsNumber; i ++){
                if(gp.selectedPet == i){
                    Point currentPt = e.getPoint();
        
                    gp.pet[i].getPoint().translate(
                        (int)(currentPt.getX() - gp.prevPt.getX()), 
                        (int)(currentPt.getY() - gp.prevPt.getY())
                    );
        
                    gp.prevPt = currentPt;
                }
            }
            for(int i = 0; i < gp.maxSelection; i++) {
                if(gp.selectedSelection == i) {
                    Point currentPt = e.getPoint();
        
                    gp.petSelection[i].getPoint().translate(
                        (int)(currentPt.getX() - gp.prevPt.getX()), 
                        (int)(currentPt.getY() - gp.prevPt.getY())
                    );
        
                    gp.prevPt = currentPt;
                }
            }
        } else {
            // System.out.println("miss tolol");
        }
    }
}
