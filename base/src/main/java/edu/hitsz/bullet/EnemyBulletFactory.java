package edu.hitsz.bullet;

/**
 * @author Chiro
 */
public class EnemyBulletFactory implements BaseBulletFactory {
    protected int locationX, locationY, speedX, speedY, power;

    public EnemyBulletFactory(int locationX, int locationY, int speedX, int speedY, int power) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.power = power;
    }

    @Override
    public BaseBullet create() {
        return new EnemyBullet(locationX, locationY, speedX, speedY, power);
    }
}
