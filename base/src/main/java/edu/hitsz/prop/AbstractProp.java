package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.vector.Vec2;

import java.util.List;

/**
 * 道具类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
abstract public class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    // @Override
    // public void forward() {
    //     super.forward();
    //
    //     // 判定 x 轴出界
    //     if (getLocationX() <= 0 || getLocationX() >= Main.WINDOW_WIDTH) {
    //         vanish();
    //     }
    //
    //     // 判定 y 轴出界
    //     if (getSpeedY() > 0 && getLocationY() >= Main.WINDOW_HEIGHT) {
    //         // 向下飞行出界
    //         vanish();
    //     } else if (getLocationY() <= 0) {
    //         // 向上飞行出界
    //         vanish();
    //     }
    // }

    /**
     * 处理飞机碰到道具的时候的反应
     *
     * @param enemyAircrafts 所有的敌机
     */
    abstract public void handleAircrafts(List<AbstractAircraft> enemyAircrafts);
}
