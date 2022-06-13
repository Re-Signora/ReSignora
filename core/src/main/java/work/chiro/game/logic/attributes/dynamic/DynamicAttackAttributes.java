package work.chiro.game.logic.attributes.dynamic;

import work.chiro.game.logic.attributes.loadable.BasicAttackAttributes;

public class DynamicAttackAttributes extends BasicDynamicAttributes {
    public static DynamicAttackAttributes initFromCharacterAttributes(BasicAttackAttributes characterAttributes) {
        return new DynamicAttackAttributes();
    }
}
