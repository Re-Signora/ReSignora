package edu.hitsz.aircraft;

/**
 * @author Chiro
 */
public class BossEnemyFactory implements AbstractAircraftFactory {
    public BossEnemyFactory() {}

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
                instance = new BossEnemy(0, 10, 2, 0, 300);
            }
        }
        return instance;
    }
}
