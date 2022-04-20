package edu.hitsz.basic;

import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public abstract class AbstractFlyingObjectFactory {
    Vec2 position;

    public AbstractFlyingObjectFactory(Vec2 posInit) {
        this.position = posInit;
    }

    public Vec2 getPosition() {
        return position;
    }
}
