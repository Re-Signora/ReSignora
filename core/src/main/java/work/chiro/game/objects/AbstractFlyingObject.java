package work.chiro.game.objects;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.utils.timer.TimeManager;
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

    public double getRadius() {
        Vec2 size = getSize();
        return Math.min(size.getX(), size.getY()) / 2;
    }

    @Override
    @Deprecated
    public boolean crash(AbstractObject<?> abstractObject, double locationX, double locationY, double width, double height) {
        return super.crash(abstractObject, locationX, locationY, width, height);
    }

    @Override
    @Deprecated
    public boolean crash(AbstractObject<?> abstractObject) {
        return super.crash(abstractObject);
    }

    public boolean crash(AbstractFlyingObject<?> abstractObject) {
        double distance = Math.sqrt(getPosition().minus(abstractObject.getPosition()).getScale().getX() + 1e-4);
        return distance < getRadius() + abstractObject.getRadius();
    }
}
