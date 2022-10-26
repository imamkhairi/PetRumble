public class Player {
    private int coin;
    private int life;
    private int win;

    public Player() {
        this.coin = 10;
        this.life = 3;
        this.win = 0;
    }

    public int getCoin() {
        return this.coin;
    }

    public int getLife() {
        return this.life;
    }

    public int getWin() {
        return this.win;
    }

    public void setCoin(int i) {
        this.coin += i;
    }

    public void resetCoin() {
        this.coin = 10;
    }

    public void minLife() {
        this.life--;
    }

    public void upWin() {
        this.win ++;
    }
    
    public void donwLife() {
        this.life --;
    }

    public void resetAll() {
        this.coin = 10;
        this.life = 3;
        this.win = 0;
    }
}
