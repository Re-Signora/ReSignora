package work.chiro.game.animate;

import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.vector.VectorFactory;
import work.chiro.game.vector.VectorType;

/**
 * 动画
 *
 * @author Chiro
 */
public class Animate {
    public static class Empty <T extends VectorType & VectorFactory<T>> extends AbstractAnimate<T> {
        Empty(T vecSource) {
            super(vecSource, AnimateType.Empty, AnimateVectorType.Others, 0, 0);
        }

        @Override
        public Boolean update(double timeNow) {
            return true;
        }

        @Override
        public T getSpeed(double timeNow) {
            return getNewVecInstance();
        }

        @Override
        public T getDelta() {
            return getNewVecInstance();
        }

        @Override
        public Boolean isDone(double timeNow) {
            return false;
        }
    }
    public static class Linear<T extends VectorType & VectorFactory<T>> extends AbstractAnimate<T> {
        private final T speed;
        private final Boolean willStop;

        public Linear(T vecSource, T speed, AnimateVectorType animateVectorType, double timeStart) {
            this(vecSource, speed, animateVectorType, timeStart, 0);
        }

        Linear(T vecSource, T speed, AnimateVectorType animateVectorType, double timeStart, double timeSpan) {
            this(vecSource, speed, animateVectorType, timeStart, timeSpan, false);
        }

        Linear(T vecSource, T speed, AnimateVectorType animateVectorType, double timeStart, double timeSpan, Boolean willStop) {
            super(vecSource, AnimateType.Linear, animateVectorType, timeStart, timeSpan);
            this.speed = speed;
            this.willStop = willStop;
        }

        public T getSpeed() {
            return speed;
        }

        @Override
        public Boolean update(double timeNow) {
            Boolean done = isDone(timeNow);
            // System.out.println("timeNow - timeStart = " + (timeNow - timeStart));
            T deltaNew = getSource().fromVector(speed.times(timeNow - timeStart));
            getVector().set(getSource().plus(deltaNew));
            return done;
        }

        @Override
        public Boolean isDone(double timeNow) {
            if (willStop && timeSpan > 0) {
                return super.isDone(timeNow);
            } else {
                return false;
            }
        }

        @Override
        public T getSpeed(double timeNow) {
            if (getAnimateVectorType() == AnimateVectorType.PositionLike) {
                return speed;
            } else {
                return getNewVecInstance();
            }
        }

        @Override
        public T getDelta() {
            return getNewVecInstance();
        }
    }

    public static class LinearLoop<T extends VectorType & VectorFactory<T>> extends Linear<T> {
        private final Vec2 range;

        LinearLoop(T vecSource, T speed, AnimateVectorType animateVectorType, double timeStart, Vec2 range) {
            super(vecSource, speed, animateVectorType, timeStart);
            this.range = range;
        }

        @Override
        public Boolean isDone(double timeNow) {
            return false;
        }

        @Override
        public Boolean update(double timeNow) {
            super.update(timeNow);
            for (int i = 0; i < getVector().getSize(); i++) {
                if (getVector().get().get(i) > range.get().get(i)) {
                    getVector().get().set(i, getVector().get().get(i) % range.get().get(i));
                }
            }
            return false;
        }
    }

    public static class LinearRebound<T extends VectorType & VectorFactory<T>> extends Linear<T> {
        private final Vec2 rangeLeft;
        private final Vec2 rangeRight;

        LinearRebound(T vecSource, T speed, double timeStart, Vec2 rangeLeft, Vec2 rangeRight) {
            this(vecSource, speed, timeStart, rangeLeft, rangeRight, 0);
        }

        LinearRebound(T vecSource, T speed, double timeStart, Vec2 rangeLeft, Vec2 rangeRight, double timeSpan) {
            super(vecSource, speed, AnimateVectorType.PositionLike, timeStart, timeSpan, true);
            this.rangeLeft = rangeLeft;
            this.rangeRight = rangeRight;
        }

        @Override
        public Boolean isDone(double timeNow) {
            return false;
        }

        @Override
        public Boolean update(double timeNow) {
            T speed = getSpeed(timeNow);
            super.update(timeNow);
            for (int i = 0; i < getVector().getSize(); i++) {
                if (getVector().get().get(i) < rangeLeft.get().get(i)) {
                    speed.get().set(i, Math.abs(speed.get().get(i)));
                    getSource().get().set(i, rangeLeft.get().get(i));
                    timeStart = timeNow;
                }
                if (getVector().get().get(i) > rangeRight.get().get(i)) {
                    speed.get().set(i, -Math.abs(speed.get().get(i)));
                    getSource().get().set(i, rangeRight.get().get(i));
                    timeStart = timeNow;
                }
            }
            return false;
        }
    }

    protected interface AnimateWithTarget<T extends VectorType> {
        /**
         * 获得 vecTarget
         *
         * @return vecTarget
         */
        T getVecTarget();
    }

    public static class LinearToTarget<T extends VectorType & VectorFactory<T>>
            extends Linear<T>
            implements AnimateWithTarget<T> {
        final private T vecTarget;
        final private double speed;
        final private boolean willStop;

        public LinearToTarget(T vecSource, T vecTarget, double speed, double timeStart, boolean willStop) {
            super(vecSource, vecTarget.copy(), AnimateVectorType.PositionLike, timeStart);
            this.vecTarget = vecTarget;
            this.speed = speed * 1000;
            this.willStop = willStop;
            updateSpeed();
        }

        public LinearToTarget(T vecSource, T vecTarget, double speed, double timeStart) {
            this(vecSource, vecTarget, speed, timeStart, true);
        }

        @Override
        public T getVecTarget() {
            return vecTarget;
        }

        public void updateSpeed() {
            T delta = getDelta();
            Scale sum = delta.getScale();
            if (sum.getX() == 0) {
                return;
            }
            getSpeed().set(getNewVecInstance().fromVector(delta.times(this.speed / sum.getX())));
        }

        @Override
        public T getDelta() {
            return getNewVecInstance().fromVector(getVecTarget().minus(getSource()));
        }

        @Override
        public Boolean isDone(double timeNow) {
            return willStop && (super.isDone(timeNow) ||
                    ((getDelta().get().get(0) > 0) ?
                            (getVector().get().get(0) >= getVecTarget().get().get(0)) :
                            (getVector().get().get(0) <= getVecTarget().get().get(0))));
        }

        @Override
        public Boolean update(double timeNow) {
            super.update(timeNow);
            if (isDone(timeNow)) {
                getVector().set(getVecTarget());
                return true;
            }
            return false;
        }
    }

    public static class LinearTracking<T extends VectorType & VectorFactory<T>>
            extends LinearToTarget<T>
            implements AnimateWithTarget<T> {
        LinearTracking(T vecSource, T target, double speed, double timeStart) {
            super(vecSource, target, speed, timeStart);
        }

        @Override
        public Boolean update(double timeNow) {
            updateSpeed();
            return super.update(timeNow);
        }
    }

    public static class NonLinear<T extends VectorType & VectorFactory<T>>
            extends AbstractAnimate<T>
            implements AnimateWithTarget<T> {
        final private T vecTarget;
        final private boolean willStop;

        NonLinear(T vecSource, T vecTarget, AnimateVectorType animateVectorType, double timeStart, double timeSpan, boolean willStop) {
            super(vecSource, AnimateType.NonLinear, animateVectorType, timeStart, timeSpan);
            this.willStop = willStop;
            this.vecTarget = vecTarget;
        }

        NonLinear(T vecSource, T vecTarget, AnimateVectorType animateVectorType, double timeStart, double timeSpan) {
            this(vecSource, vecTarget, animateVectorType, timeStart, timeSpan, true);
        }

        @Override
        public Boolean isDone(double timeNow) {
            if (willStop && timeSpan > 0) {
                return timeNow > timeStart + timeSpan;
            } else {
                return false;
            }
        }

        @Override
        public Boolean update(double timeNow) {
            double t = timeNow - timeStart;
            getVector().set(getSource().plus(getDelta().times(t * t / (timeSpan * timeSpan))));
            return isDone(timeNow);
        }

        @Override
        public T getSpeed(double timeNow) {
            double t = timeNow - timeStart;
            if (getAnimateVectorType() == AnimateVectorType.PositionLike && !isDone(timeNow)) {
                return getNewVecInstance().fromVector(getDelta().times(2 * t / (timeSpan * timeNow)));
            } else {
                return getNewVecInstance().getNewInstance();
            }
        }

        @Override
        public T getDelta() {
            return getNewVecInstance().fromVector(vecTarget.minus(getSource()));
        }

        @Override
        public T getVecTarget() {
            return vecTarget;
        }
    }

    public static class SmoothTo<T extends VectorType & VectorFactory<T>>
            extends AbstractAnimate<T>
            implements AnimateWithTarget<T> {
        final private T vecTarget;

        SmoothTo(T vecSource, T vecTarget, AnimateVectorType animateVectorType, double timeStart, double timeSpan) {
            super(vecSource, AnimateType.SmoothTo, animateVectorType, timeStart, timeSpan);
            this.vecTarget = vecTarget;
        }

        @Override
        public Boolean update(double timeNow) {
            double t = timeNow - timeStart;
            boolean done = isDone(timeNow);
            if (done) {
                getVector().set(getVecTarget());
            } else {
                double m = 2 * t * t / (timeSpan * timeSpan);
                if (t < timeSpan / 2) {
                    getVector().set(getSource().plus(getDelta().times(m)));
                } else {
                    getVector().set(getSource().plus(getDelta().times(-1 - m + 4 * t / timeSpan)));
                }
            }
            return done;
        }

        @Override
        public T getSpeed(double timeNow) {
            double t = timeNow - timeStart;
            if (getAnimateVectorType() == AnimateVectorType.PositionLike && !isDone(timeNow)) {
                if (t < timeSpan / 2) {
                    return getNewVecInstance().fromVector(getDelta().times(2.0 / timeSpan * (t / timeSpan)));
                } else {
                    return getNewVecInstance().fromVector(getDelta().times(2.0 / timeSpan * (1 - t / timeSpan)));
                }
            }
            return getNewVecInstance();
        }

        @Override
        public T getDelta() {
            return getNewVecInstance().fromVector(getVecTarget().minus(getSource()));
        }

        @Override
        public T getVecTarget() {
            return vecTarget;
        }
    }
}
