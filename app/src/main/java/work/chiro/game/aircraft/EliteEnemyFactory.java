package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.application.ImageManager;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.Constants;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class EliteEnemyFactory implements AbstractAircraftFactory {
    public EliteEnemyFactory() {
    }

    @Override
    public EliteEnemy create(AbstractConfig config) {
        Vec2 newPos = new Vec2((Math.random() * (Constants.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (Math.random() * Constants.WINDOW_HEIGHT * Constants.ELITE_CREATE_VERTICAL_RANGE));
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
