package edu.hitsz.prop;

/**
 * @author Chiro
 */
public class BombPropFactory implements AbstractPropFactory {
    protected int locationX, locationY, speedX, speedY;

    public BombPropFactory(int locationX, int locationY, int speedX, int speedY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public BombProp create() {
        return new BombProp(locationX, locationY, speedX, speedY);
    }
}
