package work.chiro.game.utils.timer;

import java.util.LinkedList;
import java.util.List;

import work.chiro.game.utils.Utils;

/**
 * 基于事件的定时器控制类
 *
 * @author Chiro
 */
public class TimerController {
    private double frameTime = 0;
    private double lastFrameTime = 0;
    private final List<Double> frameCounter = new LinkedList<>();
    private final List<Timer> timers = new LinkedList<>();

    public void init(double startTime) {
        frameTime = startTime;
    }

    synchronized public void add(Timer c) {
        timers.add(c);
    }

    synchronized public boolean remove(Timer c) {
        return timers.remove(c);
    }

    public void execute() {
        timers.forEach(timer -> timer.execute(this));
    }

    public List<Timer> getTimers() {
        return timers;
    }

    public void update() {
        frameTime = Utils.getTimeMills();
        frameCounter.add(frameTime);
        frameCounter.removeIf(t -> t < ((frameTime >= 1000) ? (frameTime - 1000) : 0));
        timers.forEach(timer -> timer.update(frameTime));
    }

    public void done() {
        lastFrameTime = frameTime;
    }

    public int getFps() {
        return frameCounter.size();
    }

    public double getTimeDelta() {
        return frameTime - lastFrameTime;
    }

    public void clear() {
        frameTime = 0;
        lastFrameTime = 0;
        frameCounter.clear();
        timers.clear();
    }
}