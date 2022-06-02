package work.chiro.game.utils.timer;

import java.util.LinkedList;
import java.util.List;

/**
 * 基于事件的定时器控制类
 *
 * @author Chiro
 */
public class TimerController {
    private double frameTime = 0;
    private double lastFrameTime = 0;
    private final LinkedList<Double> frameCounter = new LinkedList<>();
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

    synchronized public void update() {
        if (TimeManager.isPaused()) return;
        frameTime = TimeManager.getTimeMills();
        frameCounter.add(frameTime);
        frameCounter.removeIf(t -> t < ((frameTime >= 1000) ? (frameTime - 1000) : 0));
        timers.forEach(timer -> timer.update(frameTime));
    }

    public void done() {
        lastFrameTime = frameTime;
    }

    synchronized public int getFps() {
        return (int) (frameCounter.size() * 1000 / (frameCounter.getLast() - frameCounter.getFirst()));
    }

    public double getTimeDelta() {
        return frameTime - lastFrameTime;
    }

    synchronized public void clear() {
        frameTime = 0;
        lastFrameTime = 0;
        frameCounter.clear();
        timers.clear();
    }
}