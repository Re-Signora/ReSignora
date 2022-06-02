package work.chiro.game.objects.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.callback.BasicCallback;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.ResourceProvider;

/**
 * @author Chiro
 */
public class BossEnemyFactory implements AbstractAircraftFactory {
    private final BasicCallback onVanish;
    public BossEnemyFactory(BasicCallback onVanish) {
        this.onVanish = onVanish;
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
            RunningConfig.config.getEnemyMagnification().update(TimeManager.getTimeMills());
            RunningConfig.config.getEnemyMagnification().update(TimeManager.getTimeMills());
            RunningConfig.config.getBossInitialHp().update(TimeManager.getTimeMills());
            ResourceProvider.getInstance().stopMusic(MusicType.BGM);
            ResourceProvider.getInstance().startMusic(MusicType.BGM_BOSS);
            synchronized (BossEnemyFactory.class) {
                Vec2 posNew = new Vec2(0, 10);
                instance = new BossEnemy(
                        posNew,
                        new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeedRebound, posNew)
                                .setupSpeed(new Vec2(0.03 * RunningConfig.config.getEnemyMagnification().getScaleNow().getX(), 0))
                                .setupRange(new Vec2(0, 0))
                                .setupRange2(new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight))
                                .create(), RunningConfig.config.getBossInitialHp().getScaleNow().getX());
            }
        }
        instance.setOnVanish(onVanish);
        return instance;
    }
}
