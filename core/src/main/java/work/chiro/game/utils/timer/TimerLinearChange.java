package work.chiro.game.utils.timer;

import work.chiro.game.animate.AbstractAnimate;
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
    final private AbstractAnimate<Vec> animate;

    public TimerLinearChange(Scale initial, Scale change, Scale end) {
        this.initial = initial;
        if (end == null) {
            animate = new Animate.Linear<>(this.initial, change, AnimateVectorType.Others, Utils.getTimeMills());
        } else {
            animate = new Animate.LinearToTarget<>(this.initial, end, change.getX(), Utils.getTimeMills());
        }
    }

    public TimerLinearChange(double initial, double change, Double end) {
        this(new Scale(initial), new Scale(change), end == null ? null : new Scale(end));
    }

    public TimerLinearChange(double initial, double change) {
        this(new Scale(initial), new Scale(change), null);
    }

    public TimerLinearChange(double initial) {
        this(initial, 0, null);
    }

    public AbstractAnimate<Vec> getAnimate() {
        return animate;
    }

    public TimerLinearChange update(double timeNow) {
        animate.update(timeNow);
        return this;
    }

    public Scale getScaleNow() {
        return initial.fromVector(animate.getVector());
    }
}