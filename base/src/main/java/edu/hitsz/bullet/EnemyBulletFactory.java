package edu.hitsz.bullet;

import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class EnemyBulletFactory extends BaseBulletFactory {
    protected int power;

    public EnemyBulletFactory(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public BaseBullet create() {
        return new EnemyBullet(
                getPosition(),
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.ConstSpeed,
                        getPosition())
                        .setup(new Vec2(0, 0.5))
                        .create(),
                10);
    }
}
