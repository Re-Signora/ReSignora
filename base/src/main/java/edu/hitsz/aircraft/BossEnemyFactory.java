package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainerFactory;
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
                        new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeed, posNew)
                                .setup(new Vec2(0, 2))
                                .create(), 300);
            }
        }
        return instance;
    }
}
