package work.chiro.game.utils.timer;

public class TimeManager {
    private static long timeStartGlobal = 0;
    private static double timeLastValue = 0;
    private static boolean paused = false;
    private static long timePaused = 0;
    private static long timePauseStart = 0;

    public static double getTimeMills() {
//        这是个啥玩意儿？？？？有什么作用啊，没有发现有啥用，呜呜
        if (timeStartGlobal == 0) {
//            又是一个看不懂的系统函数，啊啊啊啊啊啊
            timeStartGlobal = System.currentTimeMillis();
        }
        if (paused) {
            return timeLastValue;
        }
        timeLastValue = (double) (System.currentTimeMillis() - timeStartGlobal - timePaused);
        return timeLastValue;
    }

    public static void timePause() {
        if (paused) return;
        paused = true;
        timePauseStart = System.currentTimeMillis();
    }

    public static void timeResume() {
        if (!paused) return;
        paused = false;
        timePaused += System.currentTimeMillis() - timePauseStart;
    }

    public static void timePauseToggle() {
        if (paused) timeResume();
        else timePause();
    }

    public static boolean isPaused() {
        return paused;
    }
}
