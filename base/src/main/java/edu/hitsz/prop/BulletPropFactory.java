package edu.hitsz.prop;

public class BulletPropFactory implements AbstractPropFactory {
    protected int locationX, locationY, speedX, speedY;

    public BulletPropFactory(int locationX, int locationY, int speedX, int speedY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public AbstractProp create() {
        return new BulletProp(locationX, locationY, speedX, speedY);
    }
}