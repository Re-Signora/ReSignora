package work.chiro.game.objects.attack;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class AbstractAttack extends AbstractFlyingObject {
    public AbstractAttack(Vec2 posInit, AnimateContainer animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(posInit, animateContainer, sizeInit, rotationInit, alpha);
    }
}
