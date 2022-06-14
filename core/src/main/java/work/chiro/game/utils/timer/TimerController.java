package work.chiro.game.utils.timer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import work.chiro.game.utils.Utils;

/**
 * 基于事件的定时器控制类
 *
 * @author Chiro
 */
public class TimerController {
    private double frameTime = 0;
    private double lastFrameTime = 0;
    private final LinkedList<Double> frameCounter = new LinkedList<>();
    private final Map<Object, List<Timer>> timers = new HashMap<>();
    private boolean available = true;

    public void init(double startTime) {
        frameTime = startTime;
    }

    public void disable() {
        available = false;
    }

    synchronized public void add(Object from, Timer c) {
        Utils.getLogger().warn("add({}, {})", from, c);
        if (timers.containsKey(from)) {
            timers.get(from).add(c);
        } else {
            List<Timer> list = new LinkedList<>();
            list.add(c);
            timers.put(from, list);
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    synchronized public boolean remove(Object from, Timer c) {
        Utils.getLogger().warn("remove({}, {})", from, c);
        if (timers.containsKey(from)) {
            boolean res = timers.get(from).remove(c);
            // if (timers.get(from).size() == 0) timers.remove(from);
            return res;
        } else {
            return false;
        }
    }

    synchronized public void remove(Object from) {
        Utils.getLogger().debug("Removing timer created by obj: {}", from);
        if (timers.containsKey(from)) {
            timers.get(from).forEach(timer -> Utils.getLogger().debug("\tRemoving timer: {}", timer));
            timers.get(from).clear();
            // timers.remove(from);
        }
        // DO NOT USE!!
        // timers.remove(from);
    }

    synchronized public void execute() {
        if (available) {
            timers.keySet().forEach(key -> {
                List<Timer> list = timers.get(key);
                if (list.size() == 0) return;
                for (int i = 0; i < list.size(); i++) {
                    Timer timer = list.get(i);
                    // Utils.getLogger().debug("executing [{}/{}] timer: {} from {}", i, list.size() - 1, timer, key);
                    timer.execute(this);
                }
            });
            // timers.values().forEach(timersList -> timersList.forEach(timer -> timer.execute(this)));
        }
    }

    synchronized public List<Timer> getTimers(Object from) {
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
        if (frameCounter.size() < 2) return 0;
        return (int) (frameCounter.size() * 1000 / (frameCounter.getLast() - frameCounter.getFirst() + 1e-3));
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