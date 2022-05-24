package work.chiro.game.objects.character.attributes;

public class BasicAttributes {
    private int maxHp = 400;
    private int speed = 10;
    private int ATK = 20;
    private int DEF = 5;
    /**
     * 暴击率
     */
    private int CRIT_Rate = 5;
    /**
     * 暴击伤害
     */
    private int CRIT_DMG = 105;
    /**
     * 穿透
     */
    private int pierce = 0;

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getATK() {
        return ATK;
    }

    public void setATK(int ATK) {
        this.ATK = ATK;
    }

    public int getDEF() {
        return DEF;
    }

    public void setDEF(int DEF) {
        this.DEF = DEF;
    }

    public int getCRIT_Rate() {
        return CRIT_Rate;
    }

    public void setCRIT_Rate(int CRIT_Rate) {
        this.CRIT_Rate = CRIT_Rate;
    }

    public int getCRIT_DMG() {
        return CRIT_DMG;
    }

    public void setCRIT_DMG(int CRIT_DMG) {
        this.CRIT_DMG = CRIT_DMG;
    }

    public int getPierce() {
        return pierce;
    }

    public void setPierce(int pierce) {
        this.pierce = pierce;
    }
}
