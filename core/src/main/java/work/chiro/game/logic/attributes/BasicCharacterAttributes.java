package work.chiro.game.logic.attributes;

public class BasicCharacterAttributes extends BasicThingAttributes {
    public int maxHp = 400;
    /**
     * 移动速度
     */
    public int speed = 10;
    /**
     * 攻击力
     */
    public int ATK = 20;
    /**
     * 防御力
     */
    public int DEF = 5;
    /**
     * 暴击率
     */
    public int CRIT_Rate = 5;
    /**
     * 暴击伤害
     */
    public int CRIT_DMG = 105;
    /**
     * 穿透
     */
    public int pierce = 0;
    /**
     * 普攻攻速
     */
    public double normalAttackSpeed = 5;

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

    public double getNormalAttackSpeed() {
        return normalAttackSpeed;
    }

    public double getNormalAttackDelay() {
        return 5.0 / getNormalAttackSpeed();
    }
}
