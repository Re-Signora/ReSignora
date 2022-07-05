package work.chiro.game.logic;

import java.util.Random;

import work.chiro.game.logic.attributes.dynamic.DynamicCharacterAttributes;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.logic.element.Element;
import work.chiro.game.objects.thing.attack.AbstractAttack;
import work.chiro.game.objects.thing.attack.UnderAttack;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.Utils;

/**
 * 用于伤害计算的静态类
 */
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

    /**
     * 计算一次伤害
     * @param attack 伤害对应的攻击对象
     * @param underAttack 伤害的被攻击对象
     * @return 产生的伤害
     */
    static public int calculate(AbstractAttack attack, AbstractCharacter underAttack) {
        BasicCharacterAttributes characterAttributes = attack.getSource().getBasicAttributes();
        DynamicCharacterAttributes dynamicCharacterAttributes = attack.getSource().getDynamicCharacterAttributes();
        BasicCharacterAttributes underAttackCharacterAttributes = attack.getSource().getBasicAttributes();
        DynamicCharacterAttributes underAttacksDynamicCharacterAttributes = attack.getSource().getDynamicCharacterAttributes();
        double ATK = characterAttributes.getATK();
        assert characterAttributes.getATK() + underAttackCharacterAttributes.getDEF() != 0;
        double P = (1 - characterAttributes.getPierce());
        double DEF = (1 - (P * underAttackCharacterAttributes.getDEF()) / (characterAttributes.getATK() + underAttackCharacterAttributes.getDEF()));
        // double EXT = characterAttributes.getExtraDMGRate() * 1.0 / 100;
        // TODO: fix me 额外伤害是百分数
        double EXT = characterAttributes.getExtraDMGRate() * 1.0;
        double CRIT = (isCRIT(characterAttributes.getCRIT_Rate()) ? (characterAttributes.getCRIT_DMG() * 1.0 / 100.0) : 1.0);
        Utils.getLogger().info("ATK={}, DEF={}, P={}, Ext={}, CRIT={}", ATK, DEF, P, EXT, CRIT);


//       计算buff

        int damage= (int)Math.ceil(1.0
                * ATK
                * DEF
                * P
                * EXT
                // * 特殊技能加成
                * CRIT
                *0.01
        );
        damage = underAttack.buffCaculate(Element.Pyro, damage, underAttack);
        attack.getSource().issueBuff(underAttack);
        return damage;
    }
}
