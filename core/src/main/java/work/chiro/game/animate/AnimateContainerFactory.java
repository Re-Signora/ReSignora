package work.chiro.game.animate;

import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;
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
        // 固定速度值反弹
        ConstSpeedRebound,
        // 线性匀速到某目标
        ConstSpeedToTarget,
        // 线性匀速到某目标
        ToTarget,
        // 追踪目标
        ConstSpeedTracking,
        // 非线性
        NonLinearTo,
        // 平滑过渡到
        SmoothTo,
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
        assert containerType == ContainerType.ConstSpeed ||
                containerType == ContainerType.ConstSpeedLoop ||
                containerType == ContainerType.ConstSpeedRebound ||
                containerType == ContainerType.ConstSpeedToTarget ||
                containerType == ContainerType.ConstSpeedTracking;
        this.speed2d = speed2d;
        return this;
    }

    private Double speed1d = null;

    public AnimateContainerFactory setupSpeed(Double speed1d) {
        assert containerType == ContainerType.ConstSpeedToTarget ||
                containerType == ContainerType.ConstSpeedTracking;
        this.speed1d = speed1d;
        return this;
    }

    private Vec2 target = null;

    public AnimateContainerFactory setupTarget(Vec2 target) {
        assert containerType == ContainerType.ConstSpeedTracking ||
                containerType == ContainerType.ConstSpeedToTarget ||
                containerType == ContainerType.NonLinearTo ||
                containerType == ContainerType.SmoothTo;
        this.target = target;
        return this;
    }

    private Vec2 range = null;

    public AnimateContainerFactory setupRange(Vec2 range) {
        assert containerType == ContainerType.ConstSpeedLoop ||
                containerType == ContainerType.ConstSpeedRebound;
        this.range = range;
        return this;
    }

    private Vec2 range2 = null;

    public AnimateContainerFactory setupRange2(Vec2 range2) {
        assert containerType == ContainerType.ConstSpeedRebound;
        this.range2 = range2;
        return this;
    }

    private double timeSpan = 0;

    public AnimateContainerFactory setupTimeSpan(double timeSpan) {
        assert containerType == ContainerType.ConstSpeedRebound ||
                containerType == ContainerType.NonLinearTo ||
                containerType == ContainerType.SmoothTo;
        this.timeSpan = timeSpan;
        return this;
    }

    private boolean willStop = true;

    public AnimateContainerFactory setupWillStop(boolean willStop) {
        assert containerType == ContainerType.ConstSpeedToTarget;
        this.willStop = willStop;
        return this;
    }

    private AnimateContainerCallback animateContainerCallback = null;

    public AnimateContainerFactory setupAnimateCallback(AnimateContainerCallback animateContainerCallback) {
        this.animateContainerCallback = animateContainerCallback;
        return this;
    }

    public AbstractAnimate<Vec> createAnimate() {
        switch (containerType) {
            case Empty:
                return null;
            case ConstSpeed:
                assert speed2d != null;
                return new Animate.Linear<>(position, speed2d, AnimateVectorType.PositionLike, Utils.getTimeMills());
            case ConstSpeedLoop:
                assert range != null && speed2d != null;
                return new Animate.LinearLoop<>(position, speed2d, AnimateVectorType.PositionLike, Utils.getTimeMills(), range);
            case ConstSpeedRebound:
                assert range != null && range2 != null && speed2d != null;
                return new Animate.LinearRebound<>(position, speed2d, Utils.getTimeMills(), range, range2, timeSpan);
            case ConstSpeedToTarget:
                assert target != null && speed1d != null;
                return new Animate.LinearToTarget<>(position, target, speed1d, Utils.getTimeMills(), willStop);
            case ConstSpeedTracking:
                assert speed2d != null && target != null && speed1d != null;
                return new Animate.LinearTracking<>(position, target, speed1d, Utils.getTimeMills());
            case NonLinearTo:
                assert target != null;
                return new Animate.NonLinear<>(position, target, AnimateVectorType.PositionLike, Utils.getTimeMills(), timeSpan, false);
            case SmoothTo:
                assert target != null && timeSpan != 0;
                return new Animate.SmoothTo<>(position, target, AnimateVectorType.PositionLike, Utils.getTimeMills(), timeSpan);
            default:
                break;
        }
        return null;
    }

    public AnimateContainer create() {
        AbstractAnimate<Vec> animate = createAnimate();
        if (animate == null) {
            return new AnimateContainer();
        } else {
            List<AbstractAnimate<Vec>> animateList = new LinkedList<>();
            animateList.add(animate);
            return new AnimateContainer(animateList, animateContainerCallback);
        }
    }
}
