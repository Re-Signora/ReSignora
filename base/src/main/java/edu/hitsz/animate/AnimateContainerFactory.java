package edu.hitsz.animate;

import edu.hitsz.Utils;
import edu.hitsz.vector.Vec;
import edu.hitsz.vector.Vec2;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Chiro
 */
public class AnimateContainerFactory {
    public enum ContainerType {
        // 固定速度值
        ConstSpeed,
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

    public AnimateContainerFactory setup(Vec2 speed2d) {
        assert containerType == ContainerType.ConstSpeed;
        this.speed2d = speed2d;
        return this;
    }

    public AnimateContainer create() {
        if (containerType == ContainerType.ConstSpeed) {
            return new AnimateContainer(List.of(new Animate.Linear<>(position, speed2d, AnimateVectorType.PositionLike, Utils.getTimeMills())));
        } else {
            return new AnimateContainer();
        }
    }
}
