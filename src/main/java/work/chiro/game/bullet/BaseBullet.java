package work.chiro.game.bullet;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.vector.Vec2;

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
public class BaseBullet extends AbstractFlyingObject {

    private final int power;

    public BaseBullet(Vec2 posInit, AnimateContainer animateContainer, int power) {
        super(posInit, animateContainer);
        this.power = power;
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

    public int getPower() {
        return power;
    }
}
