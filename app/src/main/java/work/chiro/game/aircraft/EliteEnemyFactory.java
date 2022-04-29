package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.application.ImageManager;
import work.chiro.game.application.Main;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class EliteEnemyFactory implements AbstractAircraftFactory {
    public EliteEnemyFactory() {
    }

    @Override
    public EliteEnemy create() {
        Vec2 newPos = new Vec2((Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (Math.random() * Main.WINDOW_HEIGHT * 0.2));
        return new EliteEnemy(
                newPos,
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.ConstSpeed, newPos)
                        .setupSpeed(new Vec2(0, 0.08))
                        .create(),
                60
        );
    }
}
