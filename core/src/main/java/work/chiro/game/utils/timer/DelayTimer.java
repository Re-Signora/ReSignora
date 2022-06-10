package work.chiro.game.utils.timer;

public class DelayTimer implements TimerCallback {
    private boolean valid = false;

    public boolean isValid() {
        return valid;
    }

    @Override
    public void run(TimerController controller, Timer timer) {
        valid = true;
        controller.remove(timer);
    }
}
