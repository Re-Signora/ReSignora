package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.application.Main;
import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class BossEnemyFactory implements AbstractAircraftFactory {
    public BossEnemyFactory() {
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
                Vec2 posNew = new Vec2(0, 10);
                instance = new BossEnemy(
                        posNew,
                        new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeedRebound, posNew)
                                .setupSpeed(new Vec2(1, 0))
                                .setupRange(new Vec2(0, 0))
                                .setupRange2(new Vec2(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT))
                                .create(), 3000);
            }
        }
        return instance;
    }
}
