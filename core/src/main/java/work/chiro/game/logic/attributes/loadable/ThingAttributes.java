package work.chiro.game.logic.attributes.loadable;

import work.chiro.game.logic.attributes.BasicThingAttributes;

public class ThingAttributes extends BasicThingAttributes {
    /**
     * 统帅
     */
    public int st = 8;
    /**
     * 隐藏伤害倍率
     */
    public int extraDMGRate = 0;

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public int getExtraDMGRate() {
        return extraDMGRate;
    }

    public void setExtraDMGRate(int extraDMGRate) {
        this.extraDMGRate = extraDMGRate;
    }
}
