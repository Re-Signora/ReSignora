package edu.hitsz.background;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class BasicBackground extends AbstractBackground {
    public BasicBackground() {
        super();
    }

    public BasicBackground(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    @Override
    AbstractBackground newInstance(Vec2 posInit, AnimateContainer animateContainer) {
        return new BasicBackground(posInit, animateContainer);
    }
}
