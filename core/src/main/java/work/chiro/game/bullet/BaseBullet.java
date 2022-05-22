package work.chiro.game.bullet;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.basic.AbstractObject;
import work.chiro.game.prop.PropHandler;
import work.chiro.game.vector.Vec2;

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
public class BaseBullet extends AbstractObject implements PropHandler {

    private final int power;

    public BaseBullet(Vec2 posInit, AnimateContainer animateContainer, int power) {
        super(posInit, animateContainer);
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    @Override
    public void onPropHandle() {
        vanish();
    }
}
