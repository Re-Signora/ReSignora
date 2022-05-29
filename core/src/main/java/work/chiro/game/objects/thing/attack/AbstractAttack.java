package work.chiro.game.objects.thing.attack;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.logic.attributes.BasicAttackAttributes;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class AbstractAttack extends AbstractThing<BasicAttackAttributes, AbstractAction> {
    public AbstractAttack(String labelName, Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, BasicAttackAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
    }
}
