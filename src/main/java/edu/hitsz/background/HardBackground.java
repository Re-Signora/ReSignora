package edu.hitsz.background;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.vector.Vec2;

import java.util.Random;

/**
 * @author Chiro
 */
public class HardBackground extends BasicBackground {
    public HardBackground() {
        super();
    }

    public HardBackground(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    @Override
    public String getImageFilename() {
        return "bg" + (new Random().nextInt(1) + 4) + ".jpg";
    }

    @Override
    AbstractBackground newInstance(Vec2 posInit, AnimateContainer animateContainer) {
        return new HardBackground(posInit, animateContainer);
    }
}
