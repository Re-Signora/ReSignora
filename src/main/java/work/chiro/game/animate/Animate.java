package work.chiro.game.animate;

import work.chiro.game.vector.Vec2;
import work.chiro.game.vector.VectorFactory;
import work.chiro.game.vector.VectorType;

/**
 * 动画
 *
 * @author Chiro
 */
public class Animate {
    public static class Linear<T extends VectorType & VectorFactory<T>> extends AbstractAnimate<T> {
        private final T speed;
        private final Boolean willStop;

        Linear(T vecSource, T speed, AnimateVectorType animateVectorType, double timeStart) {
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

    public static class LinearToTarget<T extends VectorType & VectorFactory<T>> extends Linear<T> {
        final private T target;
        final private double speed;
        LinearToTarget(T vecSource, T target, double speed, double timeStart) {
            super(vecSource, target.copy(), AnimateVectorType.PositionLike, timeStart);
            this.target = target;
            this.speed = speed;
            updateSpeed();
        }

        public T getTarget() {
            return target;
        }

        public void updateSpeed() {
            getSpeed().set(getTarget().minus(getSource()).times(this.speed));
        }
    }

    public static class LinearTracking<T extends VectorType & VectorFactory<T>> extends LinearToTarget<T> {
        LinearTracking(T vecSource, T target, double speed, double timeStart) {
            super(vecSource, target, speed, timeStart);
        }

        @Override
        public Boolean update(double timeNow) {
            updateSpeed();
            return super.update(timeNow);
        }
    }
}
