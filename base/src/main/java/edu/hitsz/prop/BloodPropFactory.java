package edu.hitsz.prop;

/**
 * @author Chiro
 */
public class BloodPropFactory implements AbstractPropFactory {
    protected int locationX, locationY, speedX, speedY, increaseHp;

    public BloodPropFactory(int locationX, int locationY, int speedX, int speedY, int increaseHp) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.increaseHp = increaseHp;
    }

    @Override
    public AbstractProp create() {
        return new BloodProp(locationX, locationY, speedX, speedY, increaseHp);
    }
}
