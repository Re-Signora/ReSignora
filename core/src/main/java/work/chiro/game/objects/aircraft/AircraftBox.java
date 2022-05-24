package work.chiro.game.objects.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.objects.AbstractObject;
import work.chiro.game.vector.Vec2;

/**
 * 绘制飞机判定框
 *
 * @author Chiro
 */
public class AircraftBox extends AbstractObject {
    public AircraftBox(Vec2 posInit, Vec2 sizeInit) {
        super(posInit,
                new AnimateContainerFactory(AnimateContainerFactory.ContainerType.Empty, posInit).create(),
                sizeInit);
    }

    @Override
    public void forward() {
        // 不改变 position
    }
}
