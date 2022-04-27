package work.chiro.game.bullet;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.vector.Vec2;

import java.util.Comparator;
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
        Vec2 p = HeroAircraftFactory.getInstance().getPosition();
        enemyAircrafts.sort(Comparator.comparing(item -> item.getPosition().minus(p).getScale().getX()));
        return new HeroBullet(
                getPosition(),
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.NonLinearTo,
                        getPosition())
                        .setupTimeSpan(300)
                        .setupTarget(enemyAircrafts.get(0).getPosition())
                        .create(),
                30);
        // return new HeroBullet(
        //         getPosition(),
        //         new AnimateContainerFactory(
        //                 AnimateContainerFactory.ContainerType.ConstSpeedToTarget,
        //                 getPosition())
        //                 .setupTarget(enemyAircrafts.get(0).getPosition().copy())
        //                 .setupSpeed(0.006)
        //                 .create(),
        //         30);
    }

    @Override
    public BaseBullet create() {
        return createDirectBullet();
    }

    @Override
    public List<BaseBullet> createMany() {
        // return List.of(createDirectBullet());
        BaseBullet trackingBullet = createTrackingBullet();
        if (trackingBullet != null) {
            return List.of(createDirectBullet(), trackingBullet);
        } else {
            return List.of(createDirectBullet());
        }
    }
}
