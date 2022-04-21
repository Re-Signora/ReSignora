package edu.hitsz.animate;

import edu.hitsz.vector.Vec;
import edu.hitsz.vector.VectorFactory;
import edu.hitsz.vector.VectorType;

/**
 * 动画
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
            getVector().fromVector(getSource().plus(deltaNew));
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
}
