package edu.hitsz.aircraft;

/**
 * @author Chiro
 */
public class EliteEnemyFactory implements AbstractAircraftFactory {
    protected int locationX, locationY, speedX, speedY, hp;

    public EliteEnemyFactory(int locationX, int locationY, int speedX, int speedY, int hp) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.hp = hp;
    }

    @Override
    public AbstractAircraft create() {
        return new EliteEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
