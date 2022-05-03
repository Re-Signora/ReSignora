package work.chiro.game.bullet;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chiro
 */
public class HeroBulletFactory extends BaseBulletFactory {
    final private List<AbstractAircraft> enemyAircrafts = new LinkedList<>();

    public HeroBulletFactory(AbstractConfig config, Vec2 posInit, List<List<? extends AbstractAircraft>> allEnemyAircrafts) {
        super(config, posInit);
        allEnemyAircrafts.forEach(enemyAircrafts::addAll);
    }

    private BaseBullet createDirectBullet(int index, int shootNum) {
        Vec2 posCopy = getPosition().copy();
        return new HeroBullet(
                config,
                posCopy,
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.ConstSpeed,
                        posCopy)
                        .setupSpeed(new Vec2((index * 2 - shootNum + 1) * 0.06, -2))
                        .create(),
                10);
    }

    private List<BaseBullet> createTrackingBullets(int required) {
        // 选择一个的敌人瞄准攻击，选择第 index 近的，如果 index > size 则剩下的全部追踪最近的
        if (enemyAircrafts.size() == 0) {
            return new ArrayList<>();
        }
        Vec2 p = HeroAircraftFactory.getInstance().getPosition();
        List<AbstractAircraft> nearEnemyAircrafts = enemyAircrafts.stream().sorted(Comparator.comparing(item -> item.getPosition().minus(p).getScale().getX())).collect(Collectors.toList());
        List<BaseBullet> res = new ArrayList<>();
        for (int i = 0; i < required; i++) {
            Vec2 posCopy = getPosition().copy();
            res.add(new HeroBullet(
                    config,
                    posCopy,
                    new AnimateContainerFactory(
                            AnimateContainerFactory.ContainerType.ConstSpeedToTarget,
                            posCopy)
                            .setupTarget(nearEnemyAircrafts.get(i >= nearEnemyAircrafts.size() ? 0 : i).getPosition().copy())
                            .setupSpeed(0.5)
                            .setupWillStop(false)
                            .create(),
                    1));
        }
        return res;
    }

    @Override
    public BaseBullet create() {
        return null;
    }

    @Override
    public List<BaseBullet> createMany(int required) {
        List<BaseBullet> res = new ArrayList<>();
        for (int i = 0; i < required; i++) {
            res.add(createDirectBullet(i, required));
        }
        res.addAll(createTrackingBullets((required - 1) * 2));
        return res;
    }
}
