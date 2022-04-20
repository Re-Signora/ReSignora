package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.application.Game;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.Utils;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodPropFactory;
import edu.hitsz.prop.BombPropFactory;
import edu.hitsz.prop.BulletPropFactory;
import edu.hitsz.vector.Vec2;

import java.util.LinkedList;
import java.util.Random;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractAircraft {

    final Random random = new Random();

    public EliteEnemy(Vec2 posInit, AnimateContainer animateContainer, int hp) {
        super(posInit, animateContainer, hp, 100);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (getLocationY() >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        return Utils.letEnemyShoot(getPosition());
    }

    @Override
    public LinkedList<AbstractProp> dropProps() {
        double select = random.nextDouble();
        double probability = 0.3;
        LinkedList <AbstractProp> props = new LinkedList<>();
        if (Utils.isInRange(select, 0, probability)) {
            props.add(new BloodPropFactory(getPosition()).create());
        } else if (Utils.isInRange(select, probability, probability * 2)) {
            props.add(new BombPropFactory(getPosition()).create());
        } else if (Utils.isInRange(select, probability * 2, probability * 3)) {
            props.add(new BulletPropFactory(getPosition()).create());
        }
        // else no props.
        return props;
    }
}
