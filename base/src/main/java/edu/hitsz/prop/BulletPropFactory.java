package edu.hitsz.prop;

/**
 * @author Chiro
 */
public class BulletPropFactory extends AbstractPropFactory {
    public BulletPropFactory(int locationX, int locationY) {
        super(locationX, locationY);
    }

    @Override
    public BulletProp create() {
        return new BulletProp(locationX, locationY);
    }
}
