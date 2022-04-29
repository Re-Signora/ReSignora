package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.application.ImageManager;
import work.chiro.game.application.Main;
import work.chiro.game.vector.Vec2;

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
                        .setupSpeed(new Vec2(0, 0.1))
                        .create(),
                30);
    }
}
