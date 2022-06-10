package work.chiro.game.utils.timer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基于事件的定时器控制类
 *
 * @author Chiro
 */
public class TimerController {
    private double frameTime = 0;
    private double lastFrameTime = 0;
    private final LinkedList<Double> frameCounter = new LinkedList<>();
    private final Map<Class<?>, List<Timer>> timers = new HashMap<>();
    private boolean available = true;

    public void init(double startTime) {
        frameTime = startTime;
    }

    public void disable() {
        available = false;
    }

    synchronized public void add(Class<?> from, Timer c) {
        if (timers.containsKey(from)) {
            timers.get(from).add(c);
        } else {
            List<Timer> list = new LinkedList<>();
            list.add(c);
            timers.put(from, list);
        }
    }

    synchronized public boolean remove(Class<?> from, Timer c) {
        if (timers.containsKey(from)) {
            return timers.get(from).remove(c);
        } else {
            return false;
        }
    }

    public void execute() {
        if (available)
            timers.values().forEach(timersList -> timersList.forEach(timer -> timer.execute(this)));
    }

    synchronized public List<Timer> getTimers(Class<?> from) {
        if (timers.containsKey(from)) {
            return timers.get(from);
        } else {
            List<Timer> list = new LinkedList<>();
            timers.put(from, list);
            return list;
        }
    }

    synchronized public void update() {
        if (TimeManager.isPaused()) return;
        frameTime = TimeManager.getTimeMills();
        frameCounter.add(frameTime);
        frameCounter.removeIf(t -> t < ((frameTime >= 1000) ? (frameTime - 1000) : 0));
        timers.values().forEach(timersList -> timersList.forEach(timer -> timer.update(frameTime)));
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