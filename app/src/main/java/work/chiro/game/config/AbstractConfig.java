package work.chiro.game.config;

import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateVectorType;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec;

/**
 * 配置参数类
 *
 * @author Chiro
 */
public abstract class AbstractConfig {
    protected static class TimerConfig {
        final private Scale initial;
        final private Scale change;
        final private Animate.Linear<Vec> animate;

        public TimerConfig(Scale initial, Scale change) {
            this.initial = initial;
            this.change = change;
            animate = new Animate.Linear<>(this.initial, this.change, AnimateVectorType.Others, Utils.getTimeMills());
        }

        public TimerConfig(double initial, double change) {
            this(new Scale(initial), new Scale(change));
        }

        public TimerConfig(double initial) {
            this(initial, 0);
        }

        public Animate.Linear<Vec> getAnimate() {
            return animate;
        }

        public TimerConfig update(double timeNow) {
            animate.update(timeNow);
            return this;
        }

        public Scale getCycleNow() {
            return initial.fromVector(animate.getVector());
        }

        public Scale getChange() {
            return change;
        }
    }

    protected TimerConfig mobCreate = new TimerConfig(700);
    protected TimerConfig eliteCreate = new TimerConfig(1200);
    protected TimerConfig enemyShoot = new TimerConfig(200);
    protected TimerConfig heroShoot = new TimerConfig(10);

    public TimerConfig getMobCreate() {
        return mobCreate;
    }

    public TimerConfig getEliteCreate() {
        return eliteCreate;
    }

    public TimerConfig getEnemyShoot() {
        return enemyShoot;
    }

    public TimerConfig getHeroShoot() {
        return heroShoot;
    }
}
