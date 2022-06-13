package work.chiro.game.logic.attributes.dynamic;

import work.chiro.game.logic.attributes.BasicThingAttributes;

public class BasicDynamicAttributes extends BasicThingAttributes {
    public boolean isEnemy = false;

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }
}
