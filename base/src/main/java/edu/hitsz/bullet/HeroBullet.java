package edu.hitsz.bullet;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.vector.Vec2;

/**
 * @author hitsz
 */
public class HeroBullet extends BaseBullet {

    public HeroBullet(Vec2 posInit, AnimateContainer animateContainer, int power) {
        super(posInit, animateContainer, power);
    }
}
