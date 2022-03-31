package edu.hitsz.aircraft;

/**
 * @author Chiro
 */
public class MobEnemyFactory implements AbstractAircraftFactory {
    protected int locationX, locationY, speedX, speedY, hp;

    public MobEnemyFactory(int locationX, int locationY, int speedX, int speedY, int hp) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.hp = hp;
    }

    @Override
    public MobEnemy create() {
        return new MobEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
