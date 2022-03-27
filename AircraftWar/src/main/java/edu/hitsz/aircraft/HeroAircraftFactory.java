package edu.hitsz.aircraft;

public class HeroAircraftFactory implements AbstractAircraftFactory {
    int locationX, locationY, speedX, speedY, hp;
    public HeroAircraftFactory(int locationX, int locationY, int speedX, int speedY, int hp) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.hp = hp;
    }

    static private HeroAircraft heroInstance = null;

    static public HeroAircraft getInstance() {
        return heroInstance;
    }

    @Override
    public HeroAircraft create() {
        if (heroInstance == null) {
            heroInstance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
        }
        return heroInstance;
    }
}
