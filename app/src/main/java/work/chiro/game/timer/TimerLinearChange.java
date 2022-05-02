package work.chiro.game.timer;

import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateVectorType;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec;

/**
 * @author Chiro
 */
public class TimerLinearChange {
    final private Scale initial;
    final private Scale change;
    final private Animate.Linear<Vec> animate;

    public TimerLinearChange(Scale initial, Scale change) {
        this.initial = initial;
        this.change = change;
        animate = new Animate.Linear<>(this.initial, this.change, AnimateVectorType.Others, Utils.getTimeMills());
    }

    public TimerLinearChange(double initial, double change) {
        this(new Scale(initial), new Scale(change));
    }

    public TimerLinearChange(double initial) {
        this(initial, 0);
    }

    public Animate.Linear<Vec> getAnimate() {
        return animate;
    }

    public TimerLinearChange update(double timeNow) {
        animate.update(timeNow);
        return this;
    }

    public Scale getScaleNow() {
        return initial.fromVector(animate.getVector());
    }

    public Scale getChange() {
        return change;
    }
}