package work.chiro.game.background;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class EasyBackground extends BasicBackground {
    public EasyBackground() {
        super();
    }

    public EasyBackground(Vec2 posInit, AnimateContainer animateContainer) {

        super(posInit, animateContainer);
    }

    @Override
    String getInitImageFilename() {
        return "bg.jpg";
    }

    @Override
    AbstractBackground newInstance(Vec2 posInit, AnimateContainer animateContainer) {
        return new EasyBackground(posInit, animateContainer);
    }
}
