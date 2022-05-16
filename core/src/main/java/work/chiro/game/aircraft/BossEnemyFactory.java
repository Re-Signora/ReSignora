package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.basic.BasicCallback;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.Constants;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.resource.MusicManager;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

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
    public BossEnemy create(AbstractConfig config) {
        if (instance == null) {
            config.getEnemyMagnification().update(Utils.getTimeMills());
            config.getBossInitialHp().update(Utils.getTimeMills());
            Utils.stopMusic(MusicManager.MusicType.BGM);
            Utils.startMusic(MusicManager.MusicType.BGM_BOSS);
            synchronized (BossEnemyFactory.class) {
                Vec2 posNew = new Vec2(0, 10);
                instance = new BossEnemy(
                        config,
                        posNew,
                        new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeedRebound, posNew)
                                .setupSpeed(new Vec2(0.03 * config.getEnemyMagnification().getScaleNow().getX(), 0))
                                .setupRange(new Vec2(0, 0))
                                .setupRange2(new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight))
                                .create(), config.getBossInitialHp().getScaleNow().getX());
            }
        }
        instance.setOnVanish(onVanish);
        return instance;
    }
}
