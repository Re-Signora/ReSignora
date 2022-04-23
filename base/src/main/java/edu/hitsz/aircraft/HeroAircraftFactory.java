package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.vector.Vec2;

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
                heroInstance = new HeroAircraft(new Vec2(Main.WINDOW_WIDTH / 2.0,
                        Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight()),
                        new AnimateContainer(), 1000);
            }
        }
        return heroInstance;
    }
}
