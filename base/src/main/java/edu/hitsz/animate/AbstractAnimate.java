package edu.hitsz.animate;

import edu.hitsz.vector.VectorType;
import edu.hitsz.vector.VectorFactory;

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
    protected final double timeStart;
    protected final double timeSpan;

    AbstractAnimate(T vecSource, AnimateType animateType, AnimateVectorType animateVectorType, double timeStart, double timeSpan) {
        this.animateType = animateType;
        this.animateVectorType = animateVectorType;
        this.vec = vecSource;
        this.source = vecSource.copy();
        this.timeStart = timeStart;
        this.timeSpan = timeSpan;
        System.out.println("start at " + timeStart);
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

    abstract public Boolean update(double timeNow);

    // abstract public Boolean isDone(double timeNow);

    public Boolean isDone(double timeNow) {
        return timeNow > timeStart + timeSpan;
    }

    abstract public T getSpeed(double timeNow);

    abstract public T getDelta();

    protected T getNewVecInstance() {
        return getSource().getNewInstance();
    }
}