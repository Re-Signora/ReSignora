package edu.hitsz.bullet;

import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class EnemyBulletFactory extends BaseBulletFactory {
    protected int power;

    EnemyBulletFactory(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public BaseBullet create() {
        return new EnemyBullet(getPosition());
    }
}
