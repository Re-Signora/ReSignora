package work.chiro.game.timer;

import work.chiro.game.utils.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * 基于事件的定时器控制类
 * @author Chiro
 */
public class TimerController {
    private static double frameTime = 0;
    private static double lastFrameTime = 0;
    private static final List<Double> FRAME_COUNTER = new LinkedList<>();
    private static final List<Timer> TIMERS = new LinkedList<>();

    public static void init(double startTime) {
        frameTime = startTime;
    }

    public static void add(Timer c) {
        TIMERS.add(c);
    }

    public static void execute() {
        TIMERS.forEach(Timer::execute);
    }

    public static List<Timer> getTimers() {
        return TIMERS;
    }

    public static void update() {
        frameTime = Utils.getTimeMills();
        FRAME_COUNTER.add(frameTime);
        FRAME_COUNTER.removeIf(t -> t < ((frameTime >= 1000) ? (frameTime - 1000) : 0));
    }

    public static void done() {
        lastFrameTime = frameTime;
    }

    public static int getFps() {
        return FRAME_COUNTER.size();
    }

    public static double getTimeDelta() {
        return frameTime - lastFrameTime;
    }

    public static void clear() {
        frameTime = 0;
        lastFrameTime = 0;
        FRAME_COUNTER.clear();
        TIMERS.clear();
    }
}