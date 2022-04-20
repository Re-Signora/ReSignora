package edu.hitsz.prop;

import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class BloodPropFactory extends AbstractPropFactory {
    public BloodPropFactory(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public BloodProp create() {
        return new BloodProp(getPosition(), 100);
    }
}
