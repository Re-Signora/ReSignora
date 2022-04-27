package work.chiro.game.bullet;

import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.vector.Vec2;

import java.util.List;

/**
 * @author Chiro
 */
public class HeroBulletFactory extends BaseBulletFactory {
    final private int index;

    public HeroBulletFactory(Vec2 posInit, int index) {
        super(posInit);
        this.index = index;
    }

    private BaseBullet createDirectBullet() {
        return new HeroBullet(
                getPosition(),
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.ConstSpeed,
                        getPosition())
                        .setupSpeed(new Vec2((index * 2 - HeroAircraftFactory.getInstance().getShootNum() + 1) * 0.06, -2))
                        .create(),
                30);
    }

    private BaseBullet createTrackingBullet() {
        // 选择一个最近的敌人瞄准攻击
        return new HeroBullet(
                getPosition(),
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.ConstSpeed,
                        getPosition())
                        .setupSpeed(new Vec2((index * 2 - HeroAircraftFactory.getInstance().getShootNum() + 1) * 0.06, -2))
                        .create(),
                30);
    }

    @Override
    public BaseBullet create() {
        return createDirectBullet();
    }

    @Override
    public List<BaseBullet> createMany() {
        return super.createMany();
    }
}
