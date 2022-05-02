package work.chiro.game.timer;

/**
 * 定时器类
 * @author Chiro
 */
public class Timer {
    private final double duration;
    private final TimerCallback callback;
    private double time = 0;

    public Timer(double duration, TimerCallback callback) {
        this.duration = duration;
        this.callback = callback;
    }

    void execute(TimerController timerController) {
        time += timerController.getTimeDelta();
        if (time >= duration) {
            time %= duration;
            callback.run();
        }
    }
}