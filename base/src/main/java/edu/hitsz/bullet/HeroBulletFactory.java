package edu.hitsz.bullet;

import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class HeroBulletFactory extends BaseBulletFactory {
    protected int power;
    HeroBulletFactory(Vec2 posInit, int power) {
        super(posInit);
        this.power = power;
    }

    @Override
    public BaseBullet create() {
        return new HeroBullet(locationX, locationY, speedX, speedY, power);
    }
}
