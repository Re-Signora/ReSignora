package work.chiro.game.compatible;

import android.view.MotionEvent;

import work.chiro.game.aircraft.HeroAircraft;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.application.Game;
import work.chiro.game.application.GameActivity;
import work.chiro.game.application.HeroController;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

public class HeroControllerAndroidImpl implements HeroController {
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
        HeroAircraft heroAircraft = HeroAircraftFactory.getInstance();
        if (heroAircraft != null) {
            Vec2 now = offset.fromVector(new Vec2(e.getX(), e.getY()).divide(scale));
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                offset = heroAircraft.getPosition().minus(now);
            }
            now.set(now.plus(offset));
            heroAircraft.setPosition(Utils.setInRange(now.getX(), 0, RunningConfig.windowWidth), Utils.setInRange(now.getY(), 0, RunningConfig.windowHeight));
        }

        Game game = GameActivity.getGame();
        if (game != null) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                game.getLayout().actionPointerPressed(getScaledPosition(e));
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                game.getLayout().actionPointerDragged(getScaledPosition(e));
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                game.getLayout().actionPointerRelease(getScaledPosition(e));
            }
        }
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
