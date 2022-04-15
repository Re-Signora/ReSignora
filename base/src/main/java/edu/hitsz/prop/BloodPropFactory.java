package edu.hitsz.prop;

/**
 * @author Chiro
 */
public class BloodPropFactory extends AbstractPropFactory {
    public BloodPropFactory(int locationX, int locationY) {
        super(locationX, locationY);
    }

    @Override
    public BloodProp create() {
        return new BloodProp(locationX, locationY, 100);
    }
}
