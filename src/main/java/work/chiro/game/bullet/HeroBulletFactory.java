package work.chiro.game.bullet;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Chiro
 */
public class HeroBulletFactory extends BaseBulletFactory {
    final private int index;
    final private List<AbstractAircraft> enemyAircrafts = new LinkedList<>();

    public HeroBulletFactory(Vec2 posInit, int index, List<List<? extends AbstractAircraft>> allEnemyAircrafts) {
        super(posInit);
        this.index = index;
        allEnemyAircrafts.forEach(enemyAircrafts::addAll);
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
        // 选择一个的敌人瞄准攻击，选择第 index 近的，如果 index > size 则剩下的全部追踪最近的
        if (enemyAircrafts.size() == 0) {
            return null;
        }
        return new HeroBullet(
                getPosition(),
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.ConstSpeedToTarget,
                        getPosition())
                        .setupTarget(enemyAircrafts.get(0).getPosition().copy())
                        .setupSpeed(0.006)
                        .create(),
                30);
    }

    @Override
    public BaseBullet create() {
        return createDirectBullet();
    }

    @Override
    public List<BaseBullet> createMany() {
        BaseBullet trackingBullet = createTrackingBullet();
        if (trackingBullet != null) {
            return List.of(createDirectBullet(), trackingBullet);
        } else {
            return List.of(createDirectBullet());
        }
    }
}
