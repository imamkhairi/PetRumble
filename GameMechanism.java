public class GameMechanism {
    public Pet player[];
    private Pet enemy[];
    private Pet playerBuff[];
    private Pet enemyBuff[];
    private int playerLvl[];
    private int enemyLvl[];
    private GamePanel gp;
    public boolean playerShifted = false;
    public boolean playerIsSaved;
    public boolean enemyIsSaved;
    public long gameTime = 0;
    public int gameResult = -1;

    public GameMechanism(Pet[] p, Pet[] e, GamePanel gp) {
        this.player = p;
        this.enemy = e;
        this.gp = gp;
        this.playerBuff = new Pet[5];
        this.enemyBuff = new Pet[5];
        this.playerLvl = new int[5];
        this.enemyLvl = new int[5];
        this.playerIsSaved = false;
        this.enemyIsSaved = false;
    }

    public void gameStart() {
        safeData(this.player, this.playerBuff, this.playerLvl, this.playerIsSaved);
        safeData(this.enemy, this.enemyBuff, this.enemyLvl, this.enemyIsSaved);
        this.enemyIsSaved = true; // false lagi setelah reset
        this.playerIsSaved = true;
        if(!isCompleted()) {
            shiftPet();
            shiftEnemy();
            if(this.playerShifted) battle(gameTime);
            isDefeated();
        } else {
            System.out.println(gameResult);

        }
        // else addEnemy();
    }

    public void addEnemy() {
        String[] newPetName = new String[this.gp.maxSelection];
        for (int i = 0; i < this.gp.maxSelection; i ++) {
            newPetName[i] = this.gp.petName.getRandomName();
            System.out.println(newPetName[i]);
            for(int j = 0; j < this.gp.maxPetsNumber; j ++) {
                if(this.enemyBuff[j] != null && this.enemyBuff[j].getFileInput().equals(newPetName[i])) {
                    // System.out.println("1 selected");
                    this.enemyBuff[j].upLevel();
                    this.enemyLvl[j] = this.enemyBuff[j].getLevel();
                    break;
                } else if(this.enemyBuff[j] == null) {
                    this.enemyBuff[j] = new Pet((int)(this.gp.setEnemyBattle[j].getX()), (int)(this.gp.setEnemyBattle[j].getY()), newPetName[i], this.gp);
                    // System.out.println("3 selected" + "index " + j);
                    break;
                } else {
                    continue;
                }
            }
        }
        for (int i = 0; i < this.gp.maxPetsNumber; i ++) {        
            if(this.enemyBuff[i] != null) System.out.println(this.enemyBuff[i].getFileInput() + " lvl " + this.enemyBuff[i].getLevel());
            
        }
    }

    public void updateEnemy() {
        for(int i = 0; i < this.gp.maxPetsNumber; i++) {
            this.enemy[i] = null;
            if(this.enemyBuff[i] != null) {
                this.enemy[i] = new Pet((int)(this.gp.setEnemyBattle[i].getX()), (int)(this.gp.setEnemyBattle[i].getY()), this.enemyBuff[i].getFileInput(), this.gp);
                for(int j = 1; j < this.enemyLvl[i]; j ++) {
                    this.enemy[i].upLevel();
                }
                this.enemyBuff[i] = null;
            }
        }
        this.enemyIsSaved = false;
    }


    public void safeData(Pet[] p, Pet[] buff, int[] lvl, boolean status) {
        if(status == false) {
            for(int i = 0; i < gp.maxPetsNumber; i ++) {
                if(p[i] != null) {
                    buff[i] = new Pet((int)(gp.setTeam[i].getX()), (int)(gp.setTeam[i].getY()), p[i].getFileInput(), gp);
                    lvl[i] = p[i].getLevel();
                    // System.out.println(i + buff[i].getFileInput());
                }
            }
            // debug
            // for(int i = 0; i < this.gp.maxPetsNumber; i++) {
            //     if(buff[i] != null) {
            //         System.out.println(i + " " + buff[i].getFileInput());
            //     }
            // }
        }
    }

    public void resetPet() {
        for(int i = 0; i < gp.maxPetsNumber; i ++) {
            player[i] = null;
            if(playerBuff[i] != null) {
                int current = playerLvl[i];
                player[i] = new Pet((int)(gp.setTeam[i].getX()), (int)(gp.setTeam[i].getY()), playerBuff[i].getFileInput(), gp);
                maintainLevel(current, player[i]);
                playerBuff[i] = null;
            } else {
                continue;
            }
            this.playerIsSaved = false;
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
                        // System.out.println("index " +j +" level " + current + this.player[j].getFileInput());
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
        if((result[0] && result[1] && result[2] && result[3] && result[4]) && (result[5] && result[6] && result[7] && result[8] && result[9])){
            gameResult = 0;
            return true;
            
        } else if (result[0] && result[1] && result[2] && result[3] && result[4]){
            gameResult = 2;
            System.out.println("enemy win");
            return true;
        } else if ((result[5] && result[6] && result[7] && result[8] && result[9])) {
            gameResult = 1;
            System.out.println("player win");
            return true;
        }
        else return false;
    }

    
}
