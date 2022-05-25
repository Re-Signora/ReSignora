package work.chiro.game.objects.thing.attack;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.logic.attributes.BasicAttackAttributes;
import work.chiro.game.objects.thing.character.AbstractThing;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class AbstractAttack extends AbstractThing<BasicAttackAttributes> {
    public AbstractAttack(String labelName, Vec2 posInit, AnimateContainer animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, BasicAttackAttributes.class, posInit, animateContainer, sizeInit, rotationInit, alpha);
    }
}
