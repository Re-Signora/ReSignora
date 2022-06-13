package work.chiro.game.logic.attributes.dynamic;

import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;

public class DynamicCharacterAttributes extends BasicDynamicAttributes {
    /**
     * 血量
     */
    public int hp = 100;
    /**
     * 元素能量
     */
    public int energy = 0;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public static DynamicCharacterAttributes initFromCharacterAttributes(BasicCharacterAttributes characterAttributes) {
        DynamicCharacterAttributes instance = new DynamicCharacterAttributes();
        instance.setHp(characterAttributes.getMaxHp());
        return instance;
    }
}
