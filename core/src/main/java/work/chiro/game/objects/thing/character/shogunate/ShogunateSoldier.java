package work.chiro.game.objects.thing.character.shogunate;

import java.io.IOException;

import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateVectorType;
import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.game.Game;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.utils.timer.DelayTimer;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.utils.timer.Timer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XImage;

@SuppressWarnings("FieldCanBeLocal")
public class ShogunateSoldier extends AbstractCharacter {
    private boolean normalAttacking = false;
    private boolean moving = false;
    private final int normalAttackDuration = 400;
    private double moveDuration = 0;
    private Timer normalAttackTask = null;
    private Timer moveTask = null;
    // private final Object mark = new Object();

    @SuppressWarnings("BusyWait")
    public ShogunateSoldier(Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("shogunate-soldier", BasicCharacterAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
        getDynamicCharacterAttributes().setEnemy(true);
        moveDuration = 100.0 / getBasicAttributes().getSpeed() * 1000;
        double normalCD = getBasicAttributes().getNormalAttackCoolDown() * 1000;
        Utils.getLogger().warn("moveDuration = {}, normalCD = {}", moveDuration, normalCD);
        // normalAttackTask = new Timer(normalCD, (controller, timer) -> {
        //     // if (isValid()) this.normalAttack();
        // });
        // moveTask = new Timer(moveDuration, (controller, timer) -> {
        //     // if (!isValid()) return;
        //     // Vec2 delta = Game.getInstance().getObjectController().getTarget().getPosition().minus(getPosition());
        //     // Vec2 newPos = delta.fromVector(getPosition().plus(delta.divide(Math.sqrt(delta.getScale().getX())).times(getBasicAttributes().getSpeed() * 20)));
        //     // moving = true;
        //     // getAnimateContainer().addAnimate(new Animate.SmoothTo<>(getPosition(), newPos, AnimateVectorType.Others, TimeManager.getTimeMills(), moveDuration / 3
        //     // ).setAnimateCallback(animate -> moving = false));
        // });
        // normalAttackTask.setName("" + this + "普通攻击");
        // moveTask.setName("" + this + "移动");

        // Game.getInstance().getTimerController().add(getClass(), normalAttackTask);
        // Game.getInstance().getTimerController().add(getClass(), moveTask);
        // Game.getInstance().getTimerController().add(mark, normalAttackTask);
        // Game.getInstance().getTimerController().add(mark, moveTask);
        // Game.getInstance().getTimerController().add(this, normalAttackTask);
        // Game.getInstance().getTimerController().add(this, moveTask);
        MyThreadFactory.getInstance().newThread(() -> {
            while (isValid()) {
                this.normalAttack();
                try {
                    Thread.sleep((long) normalCD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
        MyThreadFactory.getInstance().newThread(() -> {
            while (isValid()) {
                startMoving();
                try {
                    Thread.sleep((long) moveDuration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    public ShogunateSoldier(Vec2 posInit, AbstractAction abstractAction) {
        this(posInit, abstractAction, null, null, null);
    }

    synchronized protected void startMoving() {
        Vec2 delta = Game.getInstance().getObjectController().getTarget().getPosition().minus(getPosition());
        Vec2 newPos = delta.fromVector(getPosition().plus(delta.divide(Math.sqrt(delta.getScale().getX())).times(getBasicAttributes().getSpeed() * 20)));
        moving = true;
        getAnimateContainer().addAnimate(new Animate.SmoothTo<>(
                getPosition(),
                newPos,
                AnimateVectorType.Others,
                TimeManager.getTimeMills(),
                moveDuration / 3
        ).setAnimateCallback(animate -> moving = false));
    }

    @Override
    protected DelayTimer getDelayTimer() {
        return new DelayTimer(getClass());
    }

    public ShogunateSoldier() {
        super();
    }

    @Override
    public XImage<?> getImage(boolean getRawImage) {
        if (normalAttacking || moving) {
            try {
                return Utils.getCachedImageFromResource("characters/" + getLabelName() + "/attack.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.getImage(getRawImage);
    }

    @Override
    synchronized public void normalAttack() {
        super.normalAttack();
        normalAttacking = true;
        getAnimateContainer().addAnimate(new Animate.Delay<>(new Vec2(), normalAttackDuration).setAnimateCallback(animate -> normalAttacking = false));
    }

    @Override
    public void forward() {
        super.forward();
        setFlipped(getPosition().getX() > Game.getInstance().getObjectController().getTarget().getPosition().getX());
    }

    @Override
    public void vanish() {
        super.vanish();
        Game.getInstance().getTimerController().remove(this);
        Game.getInstance().getTimerController().remove(getClass());
        // Game.getInstance().getTimerController().remove(mark);
    }
}
