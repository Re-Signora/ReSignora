package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.Constants;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class MobEnemyFactory implements AbstractAircraftFactory {
    public MobEnemyFactory() {
    }

    @Override
    public MobEnemy create(AbstractConfig config) {
        Vec2 posNew = new Vec2(Math.random() * (Constants.WINDOW_WIDTH - ImageManager.getInstance().MOB_ENEMY_IMAGE.getWidth()),
                Math.random() * Constants.WINDOW_HEIGHT * 0.2);
        config.getEnemyMagnification().update(Utils.getTimeMills());
        return new MobEnemy(
                config,
                posNew,
                new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeed, posNew)
                        .setupSpeed(new Vec2(0, 0.1 * config.getEnemyMagnification().getScaleNow().getX()))
                        .create(),
                (30 * config.getEnemyMagnification().getScaleNow().getX()));
    }
}
