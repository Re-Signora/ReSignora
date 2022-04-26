package work.chiro.game.background;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.application.Main;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public abstract class BasicBackgroundFactory implements AbstractBackgroundFactory {
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
}
