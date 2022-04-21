package edu.hitsz.timer;

/**
 * 定时器回调接口
 * @author Chiro
 */
public interface TimerCallback {
    /**
     * 当满足定时器需求时调用。
     */
    void run();
}
