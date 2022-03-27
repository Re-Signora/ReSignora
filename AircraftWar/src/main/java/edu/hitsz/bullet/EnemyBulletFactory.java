package edu.hitsz.bullet;

public class EnemyBulletFactory implements BaseBulletFactory {
    int locationX, locationY, speedX, speedY, power;

    public EnemyBulletFactory(int locationX, int locationY, int speedX, int speedY, int power) {
        System.out.println("EnemyBulletFactory created!");
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
