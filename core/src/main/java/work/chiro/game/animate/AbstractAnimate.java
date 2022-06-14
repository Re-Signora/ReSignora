package work.chiro.game.animate;

import work.chiro.game.animate.callback.AnimateCallback;
import work.chiro.game.vector.VectorFactory;
import work.chiro.game.vector.VectorType;

/**
 * 抽象动画类，用于给 VecType 相关的量设置和时间相关的变化。
 *
 * @author Chiro
 */
public abstract class AbstractAnimate<T extends VectorType & VectorFactory<T>> {
    /**
     * 动画类型，如线性、非线性、平滑等
     */
    private final AnimateType animateType;
    /**
     * 动画应用对象类型，用于判断合成向量时是否考虑这个动画向量
     */
    private final AnimateVectorType animateVectorType;
    /**
     * 动画相关向量
     */
    private final T vec;
    /**
     * 动画相关 `vec` 的 copy
     */
    private final T source;
    /**
     * 动画开始时间
     */
    protected double timeStart;
    /**
     * 动画持续时长
     */
    protected double timeSpan;
    /**
     * 动画执行结束钩子
     */
    protected AnimateCallback<T> animateCallback = null;

    AbstractAnimate(T vecSource, AnimateType animateType, AnimateVectorType animateVectorType, double timeStart, double timeSpan) {
        this.animateType = animateType;
        this.animateVectorType = animateVectorType;
        this.vec = vecSource;
        this.source = vecSource.copy();
        this.timeStart = timeStart;
        this.timeSpan = timeSpan;
    }

    /**
     * 设置动画的开始时间，当动画需要重启的时候重新设置这个值
     *
     * @param timeStart 开始时间
     */
    public void setTimeStart(double timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * 动画的时长，当动画 `willStop` 并且 `timeNow - timeStart > timeSpan` 的时候，使得 `isDone` 返回 `true`
     *
     * @param timeSpan 动画时长
     */
    public void setTimeSpan(double timeSpan) {
        this.timeSpan = timeSpan;
    }

    public double getTimeSpan() {
        return timeSpan;
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
     * @return 动画是否已经结束
     */
    abstract public Boolean update(double timeNow);

    /**
     * 返回当前动画是否结束，默认当超过 `timeSpan` 则结束
     * @param timeNow 当前时间
     * @return 动画是否结束
     */
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

    /**
     * 返回一个当前 T 类型的新对象
     * @return new T
     */
    protected T getNewVecInstance() {
        return getSource().getNewInstance();
    }

    /**
     * 设置动画完成回调
     * @param animateCallback 动画完成回调
     * @return this
     */
    public AbstractAnimate<T> setAnimateCallback(AnimateCallback<T> animateCallback) {
        this.animateCallback = animateCallback;
        return this;
    }

    /**
     * 得到动画完成回调
     * @return 动画完成回调
     */
    public AnimateCallback<T> getAnimateCallback() {
        return animateCallback;
    }
}