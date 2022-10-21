public class GameMechanism {
    public Pet player[];
    private Pet enemy[];
    private Pet buff[];
    private int playerLvl[];
    private int enemyLvl[];
    private GamePanel gp;
    public boolean playerShifted = false;
    public boolean isSaved = false;

    public GameMechanism(Pet[] p, Pet[] e, GamePanel gp) {
        this.player = p;
        this.enemy = e;
        this.gp = gp;
        this.buff = new Pet[5];
        this.playerLvl = new int[5];
        this.enemyLvl = new int[5];
    }

    public void gameStart(long time) {
        if(!isCompleted()) {
            if(this.isSaved == false) safeData();
            shiftPet();
            shiftEnemy();
            if(this.playerShifted) battle(time);
            isDefeated();
        }
    }

    public void safeData() {
        for(int i = 0; i < gp.maxPetsNumber; i ++) {
            if(player[i] != null) {
                buff[i] = new Pet((int)(gp.setTeam[i].getX()), (int)(gp.setTeam[i].getY()), this.player[i].getFileInput(), gp);
                playerLvl[i] = player[i].getLevel();
                // System.out.println(i + buff[i].getFileInput());
            }
        }
        isSaved = true;
    }

    public void resetPet() {
        for(int i = 0; i < gp.maxPetsNumber; i ++) {
            player[i] = null;
            if(buff[i] != null) {
                int current = playerLvl[i];
                player[i] = new Pet((int)(gp.setTeam[i].getX()), (int)(gp.setTeam[i].getY()), buff[i].getFileInput(), gp);
                maintainLevel(current, player[i]);
                buff[i] = null;
            } else {
                continue;
            }
            this.isSaved = false;
            // player[i] = null;
        }
    }

    private void shiftPet() {
        for(int i = this.gp.maxPetsNumber - 1; i > 0; i --) {
            if(this.player[i] == null) {
                for(int j = i-1; j >= 0; j --) {
                    if(this.player[j] == null) {
                        continue;
                    } else if(this.player[j] != null) {
                        int current = player[j].getLevel();
                        System.out.println("index " +j +" level " + current + this.player[j].getFileInput());
                        this.player[i] = new Pet((int)(this.gp.setPlayerBattle[i].getX()), (int)(this.gp.setPlayerBattle[i].getY()), this.player[j].getFileInput(), gp);
                        maintainLevel(current, player[i]);
                        this.player[j] = null;
                        break;
                    }
                }
            }
        }
        this.playerShifted = true;
    }

    private void shiftEnemy() {
        for(int i = 0 ; i < gp.maxPetsNumber; i ++) {
            if(this.enemy[i] == null) {
                for(int j = i+1; j < gp.maxPetsNumber; j ++) {
                    if(this.enemy[j] == null) {
                        continue;
                    } else if(this.enemy[j] != null) {
                        this.enemyLvl[j] = enemy[j].getLevel();
                        // System.out.println("level " + lvl[j] + this.player[j].getFileInput());
                        this.enemy[i] = new Pet((int)(this.gp.setEnemyBattle[i].getX()), (int)(this.gp.setEnemyBattle[i].getY()), this.enemy[j].getFileInput(), gp);
                        maintainLevel(enemyLvl[j], enemy[i]);
                        this.enemy[j] = null;
                        break;
                    }
                }
            }
        }
        this.playerShifted = true;
    }

    private void maintainLevel(int current, Pet p) {
        for(int i = 1; i < current; i++) {
            p.upLevel();
        }
    }

    public void resetPanel() {
        for(int i = 0; i < gp.maxSelection; i ++) {
            gp.petSelection[i] = null;
            gp.petSelection[i] = new Pet((int)(gp.setSelection[i].getX()), (int)(gp.setSelection[i].getY()), gp.petName.getRandomName(), gp);
        }
    }

    private void battle(long time) {
        if(time >= 2000000000) {
            this.player[4].isAttacked(this.enemy[0].getAttack());
            this.enemy[0].isAttacked(this.player[4].getAttack());
        }
    }

    private void isDefeated() {
        if(enemy[0].getHealth() <= 0) enemy[0] = null;
        if(player[4].getHealth() <= 0) player[4] = null;
    }

    private boolean isCompleted() {
        boolean[] result = new boolean[2 * gp.maxPetsNumber];
        for(int i = 0; i < gp.maxPetsNumber; i++) {
            if(player[i] == null) {
                result[i] = true;
            }
            if(enemy[i] == null) {
                result[i+5] = true;
            }
        }
        if((result[0] && result[1] && result[2] && result[3] && result[4]) || (result[5] && result[6] && result[7] && result[8] && result[9])){
            System.out.println("done");
            return true;
        } 
        else return false;
    }
}
