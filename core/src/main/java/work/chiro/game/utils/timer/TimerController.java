package work.chiro.game.utils.timer;

import work.chiro.game.utils.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * 基于事件的定时器控制类
 * @author Chiro
 */
public class TimerController {
    private double frameTime = 0;
    private double lastFrameTime = 0;
    private final List<Double> FRAME_COUNTER = new LinkedList<>();
    private final List<Timer> TIMERS = new LinkedList<>();

    public void init(double startTime) {
        frameTime = startTime;
    }

    public void add(Timer c) {
        TIMERS.add(c);
    }

    public void execute() {
        TIMERS.forEach(timer -> timer.execute(this));
    }

    public List<Timer> getTimers() {
        return TIMERS;
    }

    public void update() {
        frameTime = Utils.getTimeMills();
        FRAME_COUNTER.add(frameTime);
        FRAME_COUNTER.removeIf(t -> t < ((frameTime >= 1000) ? (frameTime - 1000) : 0));
        TIMERS.forEach(timer -> timer.update(frameTime));
    }

    public void done() {
        lastFrameTime = frameTime;
    }

    public int getFps() {
        return FRAME_COUNTER.size();
    }

    public double getTimeDelta() {
        return frameTime - lastFrameTime;
    }

    public void clear() {
        frameTime = 0;
        lastFrameTime = 0;
        FRAME_COUNTER.clear();
        TIMERS.clear();
    }
}