package work.chiro.game.compatible;

import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;

import work.chiro.game.game.Game;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.ObjectController;

public class ObjectControllerAndroidImpl extends ObjectController {
    private double scale = 1.0;

    @Override
    public boolean isShootPressed() {
        return true;
    }

    private Vec2 getScaledPosition(MotionEvent e, int index) {
        try {
            return new Vec2().fromVector(new Vec2(e.getX(index), e.getY(index)).divide(scale));
        } catch (IllegalArgumentException ignored) {
            return new Vec2();
        }
    }

    private Vec2 getScaledPosition(MotionEvent e) {
        return new Vec2().fromVector(new Vec2(e.getX(), e.getY()).divide(scale));
    }

    private void handleMultiMove(MotionEvent e) {
        Game game = Game.getInstance();
        if (e.getPointerCount() > 1) {
            List<Vec2> posList = new LinkedList<>();
            for (int i = 0; i < e.getPointerCount(); i++) {
                posList.add(getScaledPosition(e, e.getPointerId(i)));
            }
            game.getTopActivity().actionPointerDragged(posList);
        } else {
            game.getTopActivity().actionPointerDragged(List.of(getScaledPosition(e)));
        }
    }

    public void onTouchEvent(MotionEvent e) {
        synchronized (MotionEvent.class) {
            Game game = Game.getInstance();
            if (game != null) {
                switch (e.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        game.getTopActivity().actionPointerPressed(getScaledPosition(e));
                        Utils.getLogger().debug("ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        game.getTopActivity().actionPointerPressed(getScaledPosition(e, e.getPointerId(e.getActionIndex())));
                        handleMultiMove(e);
                        Utils.getLogger().debug("ACTION_POINTER_DOWN: index={}, id={}", e.getActionIndex(), e.getPointerId(e.getActionIndex()));
                        break;
                    case MotionEvent.ACTION_UP:
                        game.getTopActivity().actionPointerRelease(getScaledPosition(e));
                        Utils.getLogger().debug("ACTION_UP");
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        game.getTopActivity().actionPointerRelease(getScaledPosition(e, e.getPointerId(e.getActionIndex())));
                        handleMultiMove(e);
                        Utils.getLogger().debug("ACTION_POINTER_UP: index={}, id={}", e.getActionIndex(), e.getPointerId(e.getActionIndex()));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        handleMultiMove(e);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onFrame() {
        if (lastFrameTime == null) {
            lastFrameTime = TimeManager.getTimeMills();
        }
        double now = TimeManager.getTimeMills();
        double frameTime = now - lastFrameTime;
        if (getTarget() == null) return;
        if (joySticks != null && !TimeManager.isPaused()) {
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
