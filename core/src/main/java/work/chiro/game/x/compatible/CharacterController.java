package work.chiro.game.x.compatible;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.ui.view.XJoySticks;

public abstract class CharacterController {
    protected Double lastFrameTime = null;
    protected double movingScale = 0.26;
    final public double MOVE_SPEED = 1;
    protected XJoySticks joySticks = null;

    abstract public boolean isShootPressed();

    private AbstractCharacter target = null;

    public void setTarget(AbstractCharacter target) {
        this.target = target;
    }

    public AbstractCharacter getTarget() {
        return target;
    }

    abstract public void onFrame();

    protected void setTargetPosition(Vec2 newPos) {
        getTarget().setPosition(
                Utils.setInRange(newPos.getX(), 0, RunningConfig.windowWidth),
                Utils.setInRange(newPos.getY(), 0,
                        // RunningConfig.windowHeight - getTarget().getHeight() / 2
                        RunningConfig.windowHeight
                )
        );
    }

    public void setJoySticks(XJoySticks joySticks) {
        this.joySticks = joySticks;
    }
}
