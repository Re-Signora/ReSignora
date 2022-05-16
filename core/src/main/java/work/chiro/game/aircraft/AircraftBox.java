package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

/**
 * 绘制飞机判定框
 *
 * @author Chiro
 */
public class AircraftBox extends AbstractFlyingObject {
    public AircraftBox(AbstractConfig config, Vec2 posInit, Vec2 sizeInit) {
        super(config, posInit,
                new AnimateContainerFactory(AnimateContainerFactory.ContainerType.Empty, posInit).create(),
                sizeInit);
    }

    @Override
    public void forward() {
        // 不改变 position
    }
}
