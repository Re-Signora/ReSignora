package work.chiro.game.animate;

import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.vector.VectorFactory;
import work.chiro.game.vector.VectorType;

/**
 * 动画类型
 *
 * @author Chiro
 */
public class Animate {
    /**
     * 空动画，什么也不做；不会结束，没有相关向量
     *
     * @param <T>
     */
    public static class Empty<T extends VectorType & VectorFactory<T>> extends AbstractAnimate<T> {
        public Empty(T vecSource) {
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

    /**
     * 延迟动画，用于在 AnimateContainer 中设置延时回调等
     *
     * @param <T>
     */
    public static class Delay<T extends VectorType & VectorFactory<T>> extends AbstractAnimate<T> {
        public Delay(T vecSource, double delayMs) {
            super(vecSource, AnimateType.Delay, AnimateVectorType.Others, TimeManager.getTimeMills(), delayMs);
        }

        @Override
        public Boolean update(double timeNow) {
            return isDone(timeNow);
        }

        @Override
        public T getSpeed(double timeNow) {
            return getNewVecInstance();
        }

        @Override
        public T getDelta() {
            return getNewVecInstance();
        }
    }

    /**
     * 线性变化的动画
     *
     * @param <T>
     */
    public static class Linear<T extends VectorType & VectorFactory<T>> extends AbstractAnimate<T> {
        /**
         * 变化速率向量
         */
        private final T speed;
        /**
         * 当前动画是否会结束，不结束则默认一直执行下去
         */
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

        /**
         * 按时间比例设置向量大小，其实就是依据速度确定当前时间下的向量的值
         *
         * @param timeNow 当前时间
         * @return 动画是否已经结束
         */
        @Override
        public Boolean update(double timeNow) {
            Boolean done = isDone(timeNow);
            T deltaNew = getSource().fromVector(speed.times(timeNow - timeStart));
            getVector().set(getSource().plus(deltaNew));
            return done;
        }

        /**
         * 动画是否结束；加入 `willStop` 判断
         *
         * @param timeNow 当前时间
         * @return 动画是否结束
         */
        @Override
        public Boolean isDone(double timeNow) {
            if (willStop && timeSpan > 0) {
                return super.isDone(timeNow);
            } else {
                return false;
            }
        }

        /**
         * 如果是位置相关，则返回速度向量；否则返回零向量
         *
         * @param timeNow 当前时间
         * @return 速度向量
         */
        @Override
        public T getSpeed(double timeNow) {
            if (getAnimateVectorType() == AnimateVectorType.PositionLike) {
                return speed;
            } else {
                return getNewVecInstance();
            }
        }

        /**
         * 变化矢量为零向量
         *
         * @return 零向量
         */
        @Override
        public T getDelta() {
            return getNewVecInstance();
        }
    }

    /**
     * 线性循环动画，到达边界之后自动回归初始值
     *
     * @param <T>
     */
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

        /**
         * 对向量模一下
         *
         * @param timeNow 当前时间
         * @return 动画是否完成
         */
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

    /**
     * 线性循环动画，到达边界后向量增量反向
     *
     * @param <T>
     */
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

        /**
         * update 的时候改变速度方向
         *
         * @param timeNow 当前时间
         * @return 动画是否结束
         */
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

    /**
     * 对 Animate 添加 Target 的 Feature，设置之后会到达设定的目标
     *
     * @param <T>
     */
    protected interface AnimateWithTarget<T extends VectorType> {
        /**
         * 获得 vecTarget
         *
         * @return vecTarget
         */
        T getVecTarget();
    }

    /**
     * 线性，固定速率，但是到达目标向量
     *
     * @param <T>
     */
    public static class LinearToTarget<T extends VectorType & VectorFactory<T>>
            extends Linear<T>
            implements AnimateWithTarget<T> {
        final private T vecTarget;
        final private double speed;
        final private boolean willStop;

        public LinearToTarget(T vecSource, T vecTarget, double speed, double timeStart, boolean willStop) {
            super(vecSource, vecTarget.copy(), AnimateVectorType.PositionLike, timeStart);
            this.vecTarget = vecTarget;
            // 添加一个 1000 的缩放，不然数字太小了
//            ????你这儿也没有缩放啊qwq
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

        /**
         * 更新当前速度向量
         */
        public void updateSpeed() {
            T delta = getDelta();
            Scale sum = delta.getScale();
            if (sum.getX() == 0) {
                return;
            }
            getSpeed().set(getNewVecInstance().fromVector(delta.times(this.speed / (sum.getX() == 0 ? 1e-5 : sum.getX()))));
        }

        @Override
        public T getDelta() {
            return getNewVecInstance().fromVector(getVecTarget().minus(getSource()));
        }

        /**
         * 到达目标向量算结束
         *
         * @param timeNow 当前时间
         * @return 动画是否结束
         */
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

    /**
     * 跟随某个向量，即每当被跟随的向量改变位置，使得开始点和被跟随向量始终在一条直线上；固定速率
     *
     * @param <T>
     */
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

    /**
     * 非线性动画，设定时间后到达目标位置
     *
     * @param <T>
     */
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

        /**
         * 一个二次方程计算位置
         *
         * @param timeNow 当前时间
         * @return 动画是否结束
         */
        @Override
        public Boolean update(double timeNow) {
            double t = timeNow - timeStart;
            assert timeSpan != 0;
            getVector().set(getSource().plus(getDelta().times(t * t / (timeSpan * timeSpan))));
            return isDone(timeNow);
        }

        /**
         * 对 PositionLike 更新速度向量
         *
         * @param timeNow 当前时间
         * @return 速度向量
         */
        @Override
        public T getSpeed(double timeNow) {
            double t = timeNow - timeStart;
            if (getAnimateVectorType() == AnimateVectorType.PositionLike && !isDone(timeNow)) {
                assert timeSpan * timeNow != 0;
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

    /**
     * 在规定时间后平滑移动到目标向量，前一半路程是非线性动画，后一半路程是前一半路程的相反
     *
     * @param <T>
     */
    public static class SmoothTo<T extends VectorType & VectorFactory<T>>
            extends AbstractAnimate<T>
            implements AnimateWithTarget<T> {
        final private T vecTarget;

        public SmoothTo(T vecSource, T vecTarget, AnimateVectorType animateVectorType, double timeStart, double timeSpan) {
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
                //noinspection AlibabaUndefineMagicConstant
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
                //noinspection AlibabaUndefineMagicConstant
                assert timeSpan != 0;
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
//        大小究竟在哪里啊！！！！！！
    }
}
