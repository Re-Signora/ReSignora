package edu.hitsz.animate;

import edu.hitsz.utils.Utils;
import edu.hitsz.vector.Vec2;

import java.util.List;

/**
 * @author Chiro
 */
public class AnimateContainerFactory {
    public enum ContainerType {
        // 固定速度值
        ConstSpeed,
        // 固定速度值循环
        ConstSpeedLoop,
        // 空
        Empty
    }

    private final ContainerType containerType;
    private final Vec2 position;

    public AnimateContainerFactory(ContainerType containerType, Vec2 position) {
        this.containerType = containerType;
        this.position = position;
    }

    private Vec2 speed2d = null;

    public AnimateContainerFactory setupSpeed(Vec2 speed2d) {
        assert containerType == ContainerType.ConstSpeed;
        this.speed2d = speed2d;
        return this;
    }

    private Vec2 range = null;

    public AnimateContainerFactory setupRange(Vec2 range) {
        assert containerType == ContainerType.ConstSpeedLoop;
        this.range = range;
        return this;
    }


    public AnimateContainer create() {
        if (containerType == ContainerType.ConstSpeed) {
            assert speed2d != null;
            return new AnimateContainer(List.of(new Animate.Linear<>(position, speed2d, AnimateVectorType.PositionLike, Utils.getTimeMills())));
        } else if (containerType == ContainerType.ConstSpeedLoop) {
            assert range != null && speed2d != null;
            return new AnimateContainer(List.of(new Animate.LinearLoop<>(position, speed2d, AnimateVectorType.PositionLike, Utils.getTimeMills(), range)));
        } else {
            return new AnimateContainer();
        }
    }
}
