package work.chiro.game.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * @author Chiro
 */
public class MyThreadFactory implements ThreadFactory {

    private int counter;
    private final String name;
    private final List<String> stats;
    private boolean debug = false;
    private final List<Thread> pool;

    public MyThreadFactory(String name) {
        counter = 0;
        this.name = name;
        stats = new ArrayList<>();
        pool = new ArrayList<>();
    }

    /**
     * 创建新的线程
     *
     * @param run 新线程对应任务
     * @return 线程对象
     */
    @Override
    public Thread newThread(Runnable run) {
        Thread t = new Thread(run, name + "-Thread-" + counter);
        counter++;
        String logString = String.format("Created thread %d with name %s on%s\n", t.getId(), t.getName(), new Date());
        stats.add(logString);
        if (debug) {
            System.out.print(logString);
        }
        pool.add(t);
        return t;
    }

    public List<Thread> getPool() {
        return pool;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getDebug() {
        return debug;
    }

    public String getStats() {
        StringBuilder buffer = new StringBuilder();
        for (String stat : stats) {
            buffer.append(stat);
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
