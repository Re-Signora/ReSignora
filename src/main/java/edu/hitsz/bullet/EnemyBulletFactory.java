package edu.hitsz.bullet;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.HeroAircraftFactory;
import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.application.Main;
import edu.hitsz.vector.Vec2;

import java.util.Random;

/**
 * @author Chiro
 */
public class EnemyBulletFactory extends BaseBulletFactory {
    public enum BulletType {
        // 直射
        Direct,
        // 散射
        Scatter
    }
    final private Random random = new Random();
    final private BulletType bulletType;

    public EnemyBulletFactory(Vec2 posInit, BulletType bulletType) {
        super(posInit);
        this.bulletType = bulletType;
    }

    @Override
    public BaseBullet create() {
        AnimateContainer directAnimateContainer = new AnimateContainerFactory(
                AnimateContainerFactory.ContainerType.ConstSpeed,
                getPosition())
                .setupSpeed(new Vec2(0, 0.2))
                .create();
        AnimateContainer scatterAnimateContainer = new AnimateContainerFactory(
                AnimateContainerFactory.ContainerType.ConstSpeedRebound,
                getPosition())
                // .setupSpeed(new Vec2(random.nextDouble() * 0.5 - 0.25, random.nextDouble() * 0.2 + 0.5))
                .setupSpeed(getPosition().fromVector(getPosition().minus(HeroAircraftFactory.getInstance().getPosition()).times(-0.002)))
                .setupRange(new Vec2(0, 0))
                .setupRange2(new Vec2(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT))
                .setupTimeSpan(3000)
                .create();
        return new EnemyBullet(
                getPosition(),
                bulletType == BulletType.Direct ? directAnimateContainer : scatterAnimateContainer,
                10);
    }
}
