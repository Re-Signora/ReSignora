package work.chiro.game.utils.timer;

/**
 * 定时器类
 *
 * @author Chiro
 */
public class Timer {
    private double duration;
    private final TimerCallback callback;
    private double time = 0;
    private final TimerLinearChange change;
    private String name = null;

    public Timer(TimerLinearChange change, TimerCallback callback) {
        this.duration = change.getScaleNow().getX();
        this.callback = callback;
        this.change = change;
    }

    public Timer(double duration, TimerCallback callback) {
        this.duration = duration;
        this.callback = callback;
        this.change = null;
    }

    private void judgeToRun(TimerController timerController) {
        if (time >= duration) {
            time %= duration;
            callback.run(timerController, this);
        }
    }

    public void execute(TimerController timerController) {
//        ？？？？这是什么玩意儿啊
        time += timerController.getTimeDelta();
//        frameTime - lastFrameTime时间差？不是很理解，这个差是拿来干什么的啊
        if (change != null) {
            duration = change.getScaleNow().getX();
        }
        judgeToRun(timerController);
    }

    public void update(double timeNow) {
//        就是这个拿来改成下一个的？？？？动画更新下一帧图片？
        if (change != null) {
            change.update(timeNow);
        }
    }

    @Override
    public String toString() {
        if (name == null) return super.toString();
        return "Timer{" +
                "name='" + name + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}