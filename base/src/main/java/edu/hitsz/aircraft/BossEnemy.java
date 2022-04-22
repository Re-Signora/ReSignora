package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBulletFactory;
import edu.hitsz.utils.Utils;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.vector.Vec2;

import java.util.LinkedList;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class BossEnemy extends AbstractAircraft {
    public BossEnemy(Vec2 posInit, AnimateContainer animateContainer, int hp) {
        super(posInit, animateContainer, hp, 1000);
    }

    @Override
    public void vanish() {
        super.vanish();
        BossEnemyFactory.clearInstance();
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        LinkedList<BaseBullet> ret = new LinkedList<>();
        ret.add(new EnemyBulletFactory(getPosition().copy(), EnemyBulletFactory.BulletType.Scatter).create());
        return ret;
    }

    @Override
    public LinkedList<AbstractProp> dropProps() {
        return new LinkedList<>();
    }
}
