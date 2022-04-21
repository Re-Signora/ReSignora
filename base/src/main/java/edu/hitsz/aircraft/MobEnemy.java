package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.vector.Vec2;

import java.util.LinkedList;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {
    public MobEnemy(Vec2 posInit, AnimateContainer animateContainer, int hp) {
        super(posInit, animateContainer, hp, 30);
    }

    @Override
    public void forward() {
        super.forward();
        // // 判定 y 轴向下飞行出界
        // if (getLocationY() >= Main.WINDOW_HEIGHT) {
        //     vanish();
        // }
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public LinkedList<AbstractProp> dropProps() {
        return new LinkedList<>();
    }

}
