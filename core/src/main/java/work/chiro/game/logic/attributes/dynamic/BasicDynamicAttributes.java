package work.chiro.game.logic.attributes.dynamic;

import work.chiro.game.logic.attributes.BasicThingAttributes;

/**
 * 基础动态属性类，包括是否是敌方等
 */
public class BasicDynamicAttributes extends BasicThingAttributes {
    public boolean isEnemy = false;

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }
}
