package work.chiro.game.objects.bullet;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.objects.prop.PropHandler;
import work.chiro.game.vector.Vec2;

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
public class BaseBullet extends AbstractFlyingObject implements PropHandler {

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
