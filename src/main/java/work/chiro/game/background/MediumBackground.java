package work.chiro.game.background;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.vector.Vec2;

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
    String getInitImageFilename() {
        return "bg" + (new Random().nextInt(2) + 2) + ".jpg";
    }

    @Override
    AbstractBackground newInstance(Vec2 posInit, AnimateContainer animateContainer) {
        return new MediumBackground(posInit, animateContainer);
    }
}
