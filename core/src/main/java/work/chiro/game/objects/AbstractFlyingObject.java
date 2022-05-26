package work.chiro.game.objects;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class AbstractFlyingObject<A extends AnimateContainer> extends AbstractObject<A> {
    public AbstractFlyingObject(Vec2 posInit, A animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(posInit, animateContainer, sizeInit, rotationInit, alpha);
    }

    public AbstractFlyingObject(Vec2 posInit, A animateContainer, Scale alpha) {
        super(posInit, animateContainer, alpha);
    }

    public AbstractFlyingObject(Vec2 posInit, A animateContainer) {
        super(posInit, animateContainer);
    }

    public Vec2 getAnchor() {
        return getPosition();
    }
}
