package work.chiro.game.objects.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.config.RunningConfig;
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
    public MobEnemy create() {
        Vec2 posNew = new Vec2(Math.random() * (RunningConfig.windowWidth - ImageManager.getInstance().MOB_ENEMY_IMAGE.getWidth()),
                Math.random() * RunningConfig.windowHeight * 0.2);
        RunningConfig.config.getEnemyMagnification().update(Utils.getTimeMills());
        return new MobEnemy(
                posNew,
                new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeed, posNew)
                        .setupSpeed(new Vec2(0, 0.1 * RunningConfig.config.getEnemyMagnification().getScaleNow().getX()))
                        .create(),
                (30 * RunningConfig.config.getEnemyMagnification().getScaleNow().getX()));
    }
}
