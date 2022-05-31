package work.chiro.game.compatible;

import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

import work.chiro.game.application.GameActivity;
import work.chiro.game.game.Game;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.ObjectController;

public class ObjectControllerAndroidImpl extends ObjectController {
    private double scale = 1.0;

    @Override
    public boolean isShootPressed() {
        return true;
    }

    private Vec2 getScaledPosition(MotionEvent e, int index) {
        return new Vec2().fromVector(new Vec2(e.getX(index), e.getY(index)).divide(scale));
    }

    private Vec2 getScaledPosition(MotionEvent e) {
        return new Vec2().fromVector(new Vec2(e.getX(), e.getY()).divide(scale));
    }

    public void onTouchEvent(MotionEvent e) {
        Game game = GameActivity.getGame();
        if (game != null) {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (e.getActionIndex() == 0) {
                        game.getTopActivity().actionPointerPressed(getScaledPosition(e));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    if (e.getActionIndex() == 0) {
                        game.getTopActivity().actionPointerRelease(getScaledPosition(e));
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (e.getPointerCount() > 1) {
                        List<Vec2> posList = new LinkedList<>();
                        for (int i = 0; i < e.getPointerCount(); i++) {
                            posList.add(getScaledPosition(e, e.getPointerId(i)));
                        }
                        game.getTopActivity().actionPointerDragged(posList);
                    } else {
                        game.getTopActivity().actionPointerDragged(List.of(getScaledPosition(e)));
                    }
                    break;
                default:
                    break;
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
