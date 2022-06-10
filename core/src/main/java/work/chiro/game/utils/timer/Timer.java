package work.chiro.game.utils.timer;

/**
 * 定时器类
 *
 * @author Chiro
 */
public class Timer {
    private double duration;
    private final TimerCallback callback;
    private double time = 0;
    private final TimerLinearChange change;

    public Timer(TimerLinearChange change, TimerCallback callback) {
        this.duration = change.getScaleNow().getX();
        this.callback = callback;
        this.change = change;
    }

    public Timer(double duration, TimerCallback callback) {
        this.duration = duration;
        this.callback = callback;
        this.change = null;
    }

    private void judgeToRun(TimerController timerController) {
        if (time >= duration) {
            time %= duration;
            callback.run(timerController, this);
        }
    }

    public void execute(TimerController timerController) {
        time += timerController.getTimeDelta();
        if (change != null) {
            duration = change.getScaleNow().getX();
        }
        judgeToRun(timerController);
    }

    public void update(double timeNow) {
        if (change != null) {
            change.update(timeNow);
        }
    }
}