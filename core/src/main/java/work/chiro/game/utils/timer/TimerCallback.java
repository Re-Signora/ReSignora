package work.chiro.game.utils.timer;

/**
 * 定时器回调接口
 *
 * @author Chiro
 */
public interface TimerCallback {
    void run(TimerController controller, Timer timer);
}
