package work.chiro.game.utils.timer;

public class DelayTimer implements TimerCallback {
    private boolean valid = false;

    private double timeMark = 0.0;

    public boolean isValid() {
        return valid;
    }

    public void setNotValid() {
        valid = false;
    }

    public void setValid() {
        this.valid = true;
    }

    public void setTimeMark(double timeMark) {
        this.timeMark = timeMark;
    }

    public double getTimeMark() {
        return timeMark;
    }

    @Override
    public void run(TimerController controller, Timer timer) {
        valid = true;
        controller.remove(timer);
    }
}
