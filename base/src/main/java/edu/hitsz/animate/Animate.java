package edu.hitsz.animate;

import edu.hitsz.vector.Vec;
import edu.hitsz.vector.Vec2;
import edu.hitsz.vector.VectorFactory;
import edu.hitsz.vector.VectorType;

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
                getVector().get().set(i, getVector().get().get(i) > range.get().get(i) ? getVector().get().get(i) - range.get().get(i) : getVector().get().get(i));
            }
            return false;
        }
    }
}
