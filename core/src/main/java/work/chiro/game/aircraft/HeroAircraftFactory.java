package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class HeroAircraftFactory implements AbstractAircraftFactory {
    public HeroAircraftFactory() {
    }

    /**
     * 全局唯一的 `HeroAircraft` 对象，由单例模式的双重检查锁定方法创建
     */
    static private HeroAircraft heroInstance = null;

    /**
     * 获取实例
     *
     * @return 英雄机实例
     */
    static public HeroAircraft getInstance() {
        return heroInstance;
    }

    public HeroAircraftFactory clearInstance() {
        heroInstance = null;
        return this;
    }

    @Override
    public HeroAircraft create() {
        // Double-checked locking
        if (heroInstance == null) {
            synchronized (HeroAircraftFactory.class) {
                heroInstance = new HeroAircraft(
                        new Vec2(RunningConfig.windowWidth / 2.0,
                        RunningConfig.windowHeight - ImageManager.getInstance().HERO_IMAGE.getHeight()),
                        new AnimateContainer(), new Vec2(RunningConfig.config.getHeroBoxSize(), RunningConfig.config.getHeroBoxSize()),
                        RunningConfig.config.getHeroInitialHp());
            }
        }
        return heroInstance;
    }
}
