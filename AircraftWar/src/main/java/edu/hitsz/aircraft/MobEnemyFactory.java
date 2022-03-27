package edu.hitsz.aircraft;

public class MobEnemyFactory implements AbstractAircraftFactory {
    int locationX, locationY, speedX, speedY, hp;
    public MobEnemyFactory(int locationX, int locationY, int speedX, int speedY, int hp) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.hp = hp;
    }
    @Override
    public AbstractAircraft create() {
        return new MobEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
