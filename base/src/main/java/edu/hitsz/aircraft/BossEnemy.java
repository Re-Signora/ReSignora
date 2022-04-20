package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.Utils;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.vector.Vec2;

import java.util.LinkedList;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class BossEnemy extends AbstractAircraft {

    public BossEnemy(Vec2 posInit, int speedX, int speedY, int hp) {
        super(posInit, speedX, speedY, hp, 1000);
    }

    @Override
    public void vanish() {
        super.vanish();
        BossEnemyFactory.clearInstance();
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        return Utils.letEnemyShoot(getPosition());
    }

    @Override
    public LinkedList<AbstractProp> dropProps() {
        return new LinkedList<>();
    }
}
