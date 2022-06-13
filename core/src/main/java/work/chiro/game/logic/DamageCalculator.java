package work.chiro.game.logic;

import java.util.Random;

import work.chiro.game.logic.attributes.dynamic.DynamicCharacterAttributes;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.objects.thing.attack.AbstractAttack;
import work.chiro.game.objects.thing.attack.UnderAttack;
import work.chiro.game.utils.Utils;

public class DamageCalculator {
    final static private Random random = new Random();

    /**
     * 是否暴击
     *
     * @param rate 暴击率，百分制
     * @return 是否暴击
     */
    static public boolean isCRIT(int rate) {
        return random.nextDouble() < rate * 1.0 / 100;
    }

    static public int calculate(AbstractAttack attack, UnderAttack underAttack) {
        BasicCharacterAttributes characterAttributes = attack.getSource().getBasicAttributes();
        DynamicCharacterAttributes dynamicCharacterAttributes = attack.getSource().getDynamicCharacterAttributes();
        BasicCharacterAttributes underAttackCharacterAttributes = attack.getSource().getBasicAttributes();
        DynamicCharacterAttributes underAttacksDynamicCharacterAttributes = attack.getSource().getDynamicCharacterAttributes();
        Utils.getLogger().info("ATK={}, DEF={}, P={}, Ext={}, CRIT={}",
                characterAttributes.getATK(),
                (1 - underAttackCharacterAttributes.getDEF() / (characterAttributes.getATK() + underAttackCharacterAttributes.getDEF())),
                (1 - characterAttributes.getPierce()),
                characterAttributes.getExtraDMGRate(),
                (isCRIT(characterAttributes.getCRIT_Rate()) ? (characterAttributes.getCRIT_DMG() * 1.0 / 100) : 1.0)
        );
        return (int) (1.0
                * characterAttributes.getATK()
                * (1 - underAttackCharacterAttributes.getDEF() / (characterAttributes.getATK() + underAttackCharacterAttributes.getDEF()))
                * (1 - characterAttributes.getPierce())
                * characterAttributes.getExtraDMGRate()
                // * 特殊技能加成
                * (isCRIT(characterAttributes.getCRIT_Rate()) ? (characterAttributes.getCRIT_DMG() * 1.0 / 100) : 1.0)
        );
    }
}
