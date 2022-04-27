package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.application.Main;
import work.chiro.game.application.MusicManager;
import work.chiro.game.basic.BasicCallback;
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
    public BossEnemy create() {
        if (instance == null) {
            Utils.stopMusic(MusicManager.MusicType.BGM);
            Utils.startMusic(MusicManager.MusicType.BGM_BOSS);
            synchronized (BossEnemyFactory.class) {
                Vec2 posNew = new Vec2(0, 10);
                instance = new BossEnemy(
                        posNew,
                        new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeedRebound, posNew)
                                .setupSpeed(new Vec2(0.03, 0))
                                .setupRange(new Vec2(0, 0))
                                .setupRange2(new Vec2(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT))
                                .create(), 8000);
            }
        }
        instance.setOnVanish(onVanish);
        return instance;
    }
}
