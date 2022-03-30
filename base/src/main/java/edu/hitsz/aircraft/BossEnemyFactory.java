package edu.hitsz.aircraft;

/**
 * @author Chiro
 */
public class BossEnemyFactory implements AbstractAircraftFactory {
    protected int locationX, locationY, speedX, speedY, hp;

    public BossEnemyFactory(int locationX, int locationY, int speedX, int speedY, int hp) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.hp = hp;
    }

    static private BossEnemy instance = null;

    static public BossEnemy getInstance() {
        return instance;
    }

    static public void clearInstance() {
        instance = null;
    }

    @Override
    public BossEnemy create() {
        if (instance == null) {
            System.out.println("Boss created!");
            synchronized (BossEnemyFactory.class) {
                instance = new BossEnemy(locationX, locationY, speedX, speedY, hp);
            }
        }
        return instance;
    }
}
