package work.chiro.game.animate;

import java.util.LinkedList;
import java.util.List;

import work.chiro.game.animate.callback.AnimateContainerCallback;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec;
import work.chiro.game.vector.Vec2;

/**
 * 用工厂方式生产 AnimateContainer 或者 Animate
 *
 * @author Chiro
 */
public class AnimateContainerFactory {
    // 所有支持的生成类型
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

    /**
     * 生成目标类型
     */
    private final ContainerType containerType;
    /**
     * 源位置向量
     */
    private final Vec2 position;

    public AnimateContainerFactory(ContainerType containerType, Vec2 position) {
        this.containerType = containerType;
        this.position = position;
    }

    /**
     * 2维速度向量
     */
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

    /**
     * 速度标量
     */
    private Double speed1d = null;

    public AnimateContainerFactory setupSpeed(Double speed1d) {
        assert containerType == ContainerType.ConstSpeedToTarget ||
                containerType == ContainerType.ConstSpeedTracking;
        this.speed1d = speed1d;
        return this;
    }

    /**
     * 目标向量
     */
    private Vec2 target = null;

    public AnimateContainerFactory setupTarget(Vec2 target) {
        assert containerType == ContainerType.ConstSpeedTracking ||
                containerType == ContainerType.ConstSpeedToTarget ||
                containerType == ContainerType.NonLinearTo ||
                containerType == ContainerType.SmoothTo;
        this.target = target;
        return this;
    }

    /**
     * 动画运动范围1，即开始范围
     */
    private Vec2 range = null;

    public AnimateContainerFactory setupRange(Vec2 range) {
        assert containerType == ContainerType.ConstSpeedLoop ||
                containerType == ContainerType.ConstSpeedRebound;
        this.range = range;
        return this;
    }

    /**
     * 动画运动范围2，即终止范围
     */
    private Vec2 range2 = null;

    public AnimateContainerFactory setupRange2(Vec2 range2) {
        assert containerType == ContainerType.ConstSpeedRebound;
        this.range2 = range2;
        return this;
    }

    /**
     * 设置动画持续时间
     */
    private double timeSpan = 0;

    public AnimateContainerFactory setupTimeSpan(double timeSpan) {
        assert containerType == ContainerType.ConstSpeedRebound ||
                containerType == ContainerType.NonLinearTo ||
                containerType == ContainerType.SmoothTo;
        this.timeSpan = timeSpan;
        return this;
    }

    /**
     * 设置动画是否会结束
     */
    private boolean willStop = true;

    public AnimateContainerFactory setupWillStop(boolean willStop) {
        assert containerType == ContainerType.ConstSpeedToTarget;
        this.willStop = willStop;
        return this;
    }

    /**
     * 设置动画全部完成回调
     */
    private AnimateContainerCallback animateContainerCallback = null;

    public AnimateContainerFactory setupAnimateCallback(AnimateContainerCallback animateContainerCallback) {
        this.animateContainerCallback = animateContainerCallback;
        return this;
    }

    /**
     * 根据已经留下的信息创建动画
     *
     * @return 创建的动画
     */
    public AbstractAnimate<Vec> createAnimate() {
        switch (containerType) {
            case Empty:
                return null;
            case ConstSpeed:
                assert speed2d != null;
                return new Animate.Linear<>(position, speed2d, AnimateVectorType.PositionLike, TimeManager.getTimeMills());
            case ConstSpeedLoop:
                assert range != null && speed2d != null;
                return new Animate.LinearLoop<>(position, speed2d, AnimateVectorType.PositionLike, TimeManager.getTimeMills(), range);
            case ConstSpeedRebound:
                assert range != null && range2 != null && speed2d != null;
                return new Animate.LinearRebound<>(position, speed2d, TimeManager.getTimeMills(), range, range2, timeSpan);
            case ConstSpeedToTarget:
                assert target != null && speed1d != null;
                return new Animate.LinearToTarget<>(position, target, speed1d, TimeManager.getTimeMills(), willStop);
            case ConstSpeedTracking:
                assert speed2d != null && target != null && speed1d != null;
                return new Animate.LinearTracking<>(position, target, speed1d, TimeManager.getTimeMills());
            case NonLinearTo:
                assert target != null;
                return new Animate.NonLinear<>(position, target, AnimateVectorType.PositionLike, TimeManager.getTimeMills(), timeSpan, false);
            case SmoothTo:
                assert target != null && timeSpan != 0;
                return new Animate.SmoothTo<>(position, target, AnimateVectorType.PositionLike, TimeManager.getTimeMills(), timeSpan);
            default:
                break;
        }
        return null;
    }

    /**
     * 根据留下的信息创建动画容器
     *
     * @return 创建的动画容器
     */
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
