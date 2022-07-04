package work.chiro.game.x.compatible;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.ui.view.XJoySticks;
import work.chiro.game.xactivity.BattleActivity;

public abstract class CharacterController {
    protected Double lastFrameTime = null;
    protected double movingScale = 0.26;
//    移动速度？？？？
    final public double MOVE_SPEED = 1;
    protected XJoySticks joySticks = null;

    abstract public boolean isShootPressed();

    private AbstractCharacter target = null;
    private AbstractCharacter secondaryTarget = null;

    public void setTarget(AbstractCharacter target) {
        this.target = target;
    }
//瞄准目标？？？？
    public AbstractCharacter getTarget() {
        return target;
    }
//创建目标？

    public AbstractCharacter getSecondaryTarget() {
        return secondaryTarget;
    }
//第二目标？

    public void setSecondaryTarget(AbstractCharacter secondaryTarget) {
        this.secondaryTarget = secondaryTarget;
    }

    private BattleActivity battleActivity = null;

    public BattleActivity getBattleActivity() {
        return battleActivity;
    }
//战斗动作？？
    public void setBattleActivity(BattleActivity battleActivity) {
        this.battleActivity = battleActivity;
    }

    abstract public void onFrame();

    protected void setTargetPosition(Vec2 newPos) {
//        得到目标所在坐标？？？？
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
