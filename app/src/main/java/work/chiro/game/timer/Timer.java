package work.chiro.game.timer;

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

    private void judgeToRun() {
        if (time >= duration) {
            time %= duration;
            callback.run();
        }
    }

    public void execute(TimerController timerController) {
        time += timerController.getTimeDelta();
        if (change != null) {
            duration = change.getScaleNow().getX();
        }
        judgeToRun();
    }
}