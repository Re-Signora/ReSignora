package edu.hitsz.background;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.application.Main;
import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class BasicBackgroundFactory implements AbstractBackgroundFactory {
    Vec2 position = new Vec2(0, 0);

    @Override
    public Vec2 initPosition() {
        return position;
    }

    @Override
    public AnimateContainer createAnimateContainer() {
        return new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeedLoop, position)
                .setupSpeed(new Vec2(0, 0.5))
                .setupRange(new Vec2(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT))
                .create();
    }

    @Override
    public AbstractBackground create() {
        return new BasicBackground(initPosition(), createAnimateContainer());
    }
}
