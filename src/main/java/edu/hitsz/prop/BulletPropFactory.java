package edu.hitsz.prop;

import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class BulletPropFactory extends AbstractPropFactory {
    public BulletPropFactory(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public BulletProp create() {
        return new BulletProp(getPosition(), getAnimateContainer());
    }
}
