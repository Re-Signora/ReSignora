package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

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

    @Override
    public HeroAircraft create() {
        // Double-checked locking
        if (heroInstance == null) {
            synchronized (HeroAircraftFactory.class) {
                heroInstance = new HeroAircraft(Main.WINDOW_WIDTH / 2,
                        Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                        0, 0, 100);
            }
        }
        return heroInstance;
    }
}
