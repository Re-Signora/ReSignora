package work.chiro.game.animate;

import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 动画容器，用于储存动画信息
 *
 * @author Chiro
 */
public class AnimateContainer {
    private final List<AbstractAnimate<Vec>> animateList;
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

    protected List<Boolean> updateAllInner(double timeNow) {
        return animateList.stream().map(animate -> animate.update(timeNow)).collect(Collectors.toList());
    }

    /**
     * 调用所有动画，更新当前时间下的所有动画控制的所有变量。
     *
     * @param timeNow 当前时间
     * @return 所有动画都结束了？
     */
    public Boolean updateAll(double timeNow) {
        List<Boolean> innerRes = updateAllInner(timeNow);
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

    public Vec getSpeed(double timeNow) {
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

    public Vec getDelta() {
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

    public Scale getRotation() {
        Vec delta = getDelta();
        if (delta.getSize() == 0) {
            return new Scale();
        } else {
            assert delta.getSize() == 2;
            return new Scale(-Math.atan(delta.get().get(0) / (delta.get().get(1) == 0.0 ? 1e-5 : delta.get().get(1))));
        }
    }

    public AnimateContainer clearAllAnimates() {
        getAnimateList().clear();
        return this;
    }

    public void addAnimate(AbstractAnimate<Vec> animate) {
        getAnimateList().add(animate);
    }

    public void setAnimateCallback(AnimateContainerCallback animateContainerCallback) {
        this.animateContainerCallback = animateContainerCallback;
    }

    public void clearAnimateCallback() {
        this.animateContainerCallback = null;
    }
}