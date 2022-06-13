package work.chiro.game.logic.attributes.loadable;

public class BasicCharacterAttributes
        extends ThingAttributes
        implements LoadableAttributes {
    public int maxHp = 400;
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
    /**
     * 元素战技 CD
     */
    public double skillAttackCoolDown = 8;
    /**
     * 元素爆发 CD
     */
    public double chargedAttackCoolDown = 20;

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
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

    public double getNormalAttackCoolDown() {
        assert getNormalAttackSpeed() != 0;
        return 5.0 / getNormalAttackSpeed();
    }

    public double getChargedAttackCoolDown() {
        return chargedAttackCoolDown;
    }

    public double getSkillAttackCoolDown() {
        return skillAttackCoolDown;
    }
}
