package work.chiro.game.objects.thing.attack;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.logic.attributes.dynamic.BasicDynamicAttributes;
import work.chiro.game.logic.attributes.dynamic.DynamicAttackAttributes;
import work.chiro.game.logic.attributes.loadable.BasicAttackAttributes;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class AbstractAttack extends AbstractThing<BasicAttackAttributes, AbstractAction> {
    protected final AbstractCharacter source;
    protected final DynamicAttackAttributes dynamicAttackAttributes;

    public AbstractAttack(AbstractCharacter source, String labelName, Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, BasicAttackAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
        this.source = source;
        dynamicAttackAttributes = DynamicAttackAttributes.initFromCharacterAttributes((BasicAttackAttributes) getBasicAttributes());
    }

    public AbstractCharacter getSource() {
        return source;
    }


    public DynamicAttackAttributes getDynamicAttackAttributes() {
        return dynamicAttackAttributes;
    }

    public AbstractAttack setEnemy(boolean isEnemy) {
        getDynamicAttackAttributes().setEnemy(isEnemy);
        return this;
    }

    @Override
    public BasicDynamicAttributes getBasicDynamicAttributes() {
        return getDynamicAttackAttributes();
    }
}
