package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class MobEnemyFactory implements AbstractAircraftFactory {
    public MobEnemyFactory() {
    }

    @Override
    public MobEnemy create() {
        Vec2 posNew = new Vec2(Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()),
                Math.random() * Main.WINDOW_HEIGHT * 0.2);
        return new MobEnemy(
                posNew,
                new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeed, posNew)
                        .setup(new Vec2(0, 0.1))
                        .create(),
                30);
    }
}
