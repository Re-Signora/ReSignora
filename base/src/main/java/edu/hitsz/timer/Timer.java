package edu.hitsz.timer;

/**
 * 定时器类
 * @author Chiro
 */
public class Timer {
    public final double duration;
    private final TimerCallback callback;
    public double time = 0;

    public Timer(double duration, TimerCallback callback) {
        this.duration = duration;
        this.callback = callback;
    }

    void execute() {
        time += TimerController.getTimeDelta();
        if (time >= duration) {
            time %= duration;
            callback.run();
        }
    }
}