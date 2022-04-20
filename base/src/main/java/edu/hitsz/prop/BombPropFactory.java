package edu.hitsz.prop;

import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class BombPropFactory extends AbstractPropFactory {
    public BombPropFactory(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public BombProp create() {
        return new BombProp(getPosition());
    }
}
