package edu.hitsz.aircraft;

public class HeroAircraftFactory implements AbstractAircraftFactory {
    protected int locationX, locationY, speedX, speedY, hp;
    public HeroAircraftFactory(int locationX, int locationY, int speedX, int speedY, int hp) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.hp = hp;
    }

    // 全局唯一的 `HeroAircraft` 对象，由单例模式的双重检查锁定方法创建
    static private HeroAircraft heroInstance = null;
    // 获取实例
    static public HeroAircraft getInstance() {
        return heroInstance;
    }

    @Override
    public HeroAircraft create() {
        // Double-checked locking
        if (heroInstance == null) {
            synchronized (HeroAircraftFactory.class) {
                heroInstance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
            }
        }
        return heroInstance;
    }
}
