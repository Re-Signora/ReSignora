package work.chiro.game.objects.thing.attack;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.logic.attributes.dynamic.BasicDynamicAttributes;
import work.chiro.game.logic.attributes.dynamic.DynamicAttackAttributes;
import work.chiro.game.logic.attributes.loadable.BasicAttackAttributes;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

/**
 * 抽象攻击类
 */
public class AbstractAttack extends AbstractThing<BasicAttackAttributes, AbstractAction> {
    /**
     * 攻击发出者
     */
    protected final AbstractCharacter source;
    /**
     * 攻击对应动态属性
     */
    protected final DynamicAttackAttributes dynamicAttackAttributes;
    /**
     * 攻击发类型
     */
    protected AttackKind attackKind;

    public AbstractAttack(AbstractCharacter source, String labelName, Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, BasicAttackAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
        this.source = source;
        if (source != null) setEnemy(source.isEnemy());
        dynamicAttackAttributes = DynamicAttackAttributes.initFromCharacterAttributes((BasicAttackAttributes) getBasicAttributes());
    }

    public AbstractCharacter getSource() {
        return source;
    }

    public DynamicAttackAttributes getDynamicAttackAttributes() {
        return dynamicAttackAttributes;
    }

    /**
     * 设置是否为敌方
     *
     * @param isEnemy 是否为敌方
     * @return this
     */
    public AbstractAttack setEnemy(boolean isEnemy) {
        if (getDynamicAttackAttributes() != null)
            getDynamicAttackAttributes().setEnemy(isEnemy);
        return this;
    }

    @Override
    public BasicDynamicAttributes getBasicDynamicAttributes() {
        return getDynamicAttackAttributes();
    }

    /**
     * 返回是否为敌方
     *
     * @return 是否为敌方
     */
    public boolean isEnemy() {
        if (getDynamicAttackAttributes() != null)
            return getDynamicAttackAttributes().isEnemy();
        return false;
    }
}
