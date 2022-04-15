package edu.hitsz.prop;

/**
 * @author Chiro
 */
public class BombPropFactory extends AbstractPropFactory {
    public BombPropFactory(int locationX, int locationY) {
        super(locationX, locationY);
    }

    @Override
    public BombProp create() {
        return new BombProp(locationX, locationY);
    }
}
