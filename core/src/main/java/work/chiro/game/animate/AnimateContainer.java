package work.chiro.game.animate;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import work.chiro.game.animate.callback.AnimateContainerCallback;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec;

/**
 * 动画容器，用于储存动画信息
 *
 * @author Chiro
 */
public class AnimateContainer {
    /**
     * 储存的动画列表
     */
    private final List<AbstractAnimate<Vec>> animateList;
    /**
     * 动画全部完成的钩子
     */
    private AnimateContainerCallback animateContainerCallback;

    public AnimateContainer() {
        this(new LinkedList<>());
    }

    public AnimateContainer(List<AbstractAnimate<Vec>> animateList) {
        this(animateList, null);
    }

    public AnimateContainer(List<AbstractAnimate<Vec>> animateList, AnimateContainerCallback animateContainerCallback) {
        this.animateList = animateList;
        this.animateContainerCallback = animateContainerCallback;
    }

    public List<AbstractAnimate<Vec>> getAnimateList() {
        return animateList;
    }

    /**
     * 内部调用的 updateAll；谨慎起见加上了线程锁
     *
     * @param timeNow 当前时间
     * @return 动画列表中对应动画是否完成
     */
    synchronized protected List<Boolean> updateAllInner(double timeNow) {
        return animateList.stream().map(animate -> animate.update(timeNow)).collect(Collectors.toList());
    }

    /**
     * 调用所有动画，更新当前时间下的所有动画控制的所有变量。
     *
     * @param timeNow 当前时间
     * @return 所有动画都结束了？
     */
    synchronized public Boolean updateAll(double timeNow) {
        List<Boolean> innerRes = updateAllInner(timeNow);
        // 统计动画是否全部完成
        boolean finished = innerRes.isEmpty() || (innerRes.stream().mapToInt(res -> res ? 0 : 1).sum() == 0);
        if (finished && animateContainerCallback != null) {
            boolean allFinished = animateContainerCallback.onFinish(this);
            if (allFinished) {
                clearAnimateCallback();
            }
            return allFinished;
        }
        return finished;
    }

    /**
     * 获取所管理的动画的合速度向量；仅对 PositionLike 作用类型有效
     *
     * @param timeNow 当前时间
     * @return 合成的速度向量
     */
    synchronized public Vec getSpeed(double timeNow) {
        if (animateList.isEmpty()) {
            return new Vec();
        } else {
            Optional<Vec> speed = animateList.stream()
                    .filter(animate -> animate.getAnimateVectorType() == AnimateVectorType.PositionLike)
                    .map(animate -> animate.getSpeed(timeNow)).reduce(Vec::plus);
            if (speed.isEmpty()) {
                return new Vec();
            } else {
                return speed.get();
            }
        }
    }

    /**
     * 获取所管理的动画的合成位移向量；仅对 PositionLike 作用类型有效
     *
     * @return 合成位移向量
     */
    synchronized public Vec getDelta() {
        if (animateList.isEmpty()) {
            return new Vec();
        } else {
            Optional<Vec> delta = animateList.stream()
                    .filter(animate -> animate.getAnimateVectorType() == AnimateVectorType.PositionLike)
                    .map(AbstractAnimate::getDelta).reduce(Vec::plus);
            if (delta.isEmpty()) {
                return new Vec();
            } else {
                return delta.get();
            }
        }
    }

    /**
     * 获取所有动画的合成旋转角度标量
     *
     * @return 合成旋转角度标量
     */
    synchronized public Scale getRotation() {
        Vec delta = getDelta();
        if (delta.getSize() == 0) {
            return new Scale();
        } else {
            assert delta.getSize() == 2;
            return new Scale(-Math.atan(delta.get().get(1) / (delta.get().get(0) == 0.0 ? 1e-5 : delta.get().get(0))));
        }
    }

    /**
     * 清除所有动画
     *
     * @return this
     */
    public AnimateContainer clearAllAnimates() {
        getAnimateList().clear();
        return this;
    }

    /**
     * 添加一个动画
     *
     * @param animate 待添加动画
     */
    public void addAnimate(AbstractAnimate<Vec> animate) {
        getAnimateList().add(animate);
    }

    /**
     * 移除一个动画
     *
     * @param animate 待移除动画
     * @return 移除是否成功
     */
    public boolean removeAnimate(AbstractAnimate<Vec> animate) {
        return getAnimateList().remove(animate);
    }

    /**
     * 设置动画全部完成回调
     *
     * @param animateContainerCallback 动画全部完成回调
     */
    public void setAnimateCallback(AnimateContainerCallback animateContainerCallback) {
        this.animateContainerCallback = animateContainerCallback;
    }

    /**
     * 清除动画全部完成回调
     */
    public void clearAnimateCallback() {
        this.animateContainerCallback = null;
    }
}