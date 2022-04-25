package edu.hitsz.background;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.vector.Vec2;

import java.util.Random;

/**
 * @author Chiro
 */
public class MediumBackground extends BasicBackground {
    public MediumBackground() {
        super();
    }

    public MediumBackground(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    @Override
    public String getImageFilename() {
        return "bg" + (new Random().nextInt(2) + 2) + ".jpg";
    }

    @Override
    AbstractBackground newInstance(Vec2 posInit, AnimateContainer animateContainer) {
        return new MediumBackground(posInit, animateContainer);
    }
}
