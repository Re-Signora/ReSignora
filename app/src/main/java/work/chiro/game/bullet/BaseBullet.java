package work.chiro.game.bullet;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
public class BaseBullet extends AbstractFlyingObject {

    private final int power;

    public BaseBullet(AbstractConfig config, Vec2 posInit, AnimateContainer animateContainer, int power) {
        super(config, posInit, animateContainer);
        this.power = power;
    }

    public int getPower() {
        return power;
    }
}
