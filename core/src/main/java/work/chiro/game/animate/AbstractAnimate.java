package work.chiro.game.animate;

import work.chiro.game.animate.callback.AnimateCallback;
import work.chiro.game.vector.VectorFactory;
import work.chiro.game.vector.VectorType;

/**
 * 抽象动画类
 *
 * @author Chiro
 */
public abstract class AbstractAnimate<T extends VectorType & VectorFactory<T>> {
    private final AnimateType animateType;
    private final AnimateVectorType animateVectorType;
    private final T vec;
    private final T source;
    protected double timeStart;
    protected final double timeSpan;
    protected AnimateCallback animateCallback = null;

    AbstractAnimate(T vecSource, AnimateType animateType, AnimateVectorType animateVectorType, double timeStart, double timeSpan) {
        this.animateType = animateType;
        this.animateVectorType = animateVectorType;
        this.vec = vecSource;
        this.source = vecSource.copy();
        this.timeStart = timeStart;
        this.timeSpan = timeSpan;
    }

    public AnimateType getAnimateType() {
        return animateType;
    }

    public AnimateVectorType getAnimateVectorType() {
        return animateVectorType;
    }

    public T getVector() {
        return vec;
    }

    public T getSource() {
        return source;
    }

    /**
     * 当动画需要被更新时调用，更新该动画所管理的随时间变化的变量。
     *
     * @param timeNow 当前时间
     * @return 是否更新
     */
    abstract public Boolean update(double timeNow);

    // abstract public Boolean isDone(double timeNow);

    public Boolean isDone(double timeNow) {
        boolean done = timeNow > timeStart + timeSpan;
        if (done && animateCallback != null) animateCallback.onFinish(this);
        return done;
    }

    /**
     * 得到当前动画的变化速度的向量，一位或者二维
     *
     * @param timeNow 当前时间
     * @return 速度向量
     */
    abstract public T getSpeed(double timeNow);

    /**
     * 得到当前动画的位置变化矢量，一维或者二维
     *
     * @return 变化矢量
     */
    abstract public T getDelta();

    protected T getNewVecInstance() {
        return getSource().getNewInstance();
    }

    public void setAnimateCallback(AnimateCallback animateCallback) {
        this.animateCallback = animateCallback;
    }

    public AnimateCallback getAnimateCallback() {
        return animateCallback;
    }
}