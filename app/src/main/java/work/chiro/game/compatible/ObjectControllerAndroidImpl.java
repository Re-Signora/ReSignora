package work.chiro.game.compatible;

import android.view.MotionEvent;

import work.chiro.game.application.GameActivity;
import work.chiro.game.game.Game;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.ObjectController;

public class ObjectControllerAndroidImpl extends ObjectController {
    private Vec2 offset = new Vec2();
    private double scale = 1.0;

    @Override
    public boolean isShootPressed() {
        return true;
    }

    public void reset() {
        offset = new Vec2();
    }

    private Vec2 getScaledPosition(MotionEvent e) {
        return new Vec2().fromVector(new Vec2(e.getX(), e.getY()).divide(scale));
    }

    public void onTouchEvent(MotionEvent e) {
        Game game = GameActivity.getGame();
        if (game != null) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                game.getTopActivity().actionPointerPressed(getScaledPosition(e));
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                game.getTopActivity().actionPointerDragged(getScaledPosition(e));
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                game.getTopActivity().actionPointerRelease(getScaledPosition(e));
            }
        }
    }

    @Override
    public void onFrame() {
        if (lastFrameTime == null) {
            lastFrameTime = Utils.getTimeMills();
        }
        double now = Utils.getTimeMills();
        double frameTime = now - lastFrameTime;
        if (getTarget() == null) return;
        if (joySticks != null) {
            Vec2 speed = joySticks.getPointedSpeed();
            double r = Math.sqrt(speed.getScale().getX());
            Vec2 next;
            if (r < 0.25) {
                next = new Vec2();
            } else if (r < 0.9) {
                next = speed.fromVector(speed.divide(r).times(0.5));
            } else {
                next = speed;
            }
            Vec2 newPos = getTarget().getPosition().plus(next.fromVector(next.times(frameTime * MOVE_SPEED)));
            setTargetPosition(newPos);
        }
        lastFrameTime = now;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
