package work.chiro.game.objects.thing.character.shogunate;

import java.io.IOException;

import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateVectorType;
import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.game.Game;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.Utils;
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
    private final int normalAttackDuration = 1000;
    private double moveDuration = 0;
    private Timer normalAttackTask = null;
    private Timer moveTask = null;

    public ShogunateSoldier(Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("shogunate-soldier", BasicCharacterAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
        moveDuration = 100.0 / getBasicAttributes().getSpeed() * 1000;
        normalAttackTask = new Timer(getBasicAttributes().getNormalAttackCoolDown() * 1000, (controller, timer) -> this.normalAttack());
        moveTask = new Timer(moveDuration, (controller, timer) -> {
            Vec2 delta = Game.getInstance().getObjectController().getTarget().getPosition().minus(getPosition());
            Vec2 newPos = delta.fromVector(getPosition().plus(delta.divide(Math.sqrt(delta.getScale().getX())).times(getBasicAttributes().getSpeed() * 20)));
            moving = true;
            getAnimateContainer().addAnimate(new Animate.SmoothTo<>(getPosition(), newPos, AnimateVectorType.Others, TimeManager.getTimeMills(), moveDuration / 3
            ).setAnimateCallback(animate -> moving = false));
        });
        Game.getInstance().getTimerController().add(getClass(), normalAttackTask);
        Game.getInstance().getTimerController().add(getClass(), moveTask);
    }

    public ShogunateSoldier(Vec2 posInit, AbstractAction abstractAction) {
        this(posInit, abstractAction, null, null, null);
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
    public void normalAttack() {
        super.normalAttack();
        normalAttacking = true;
        getAnimateContainer().addAnimate(new Animate.Delay<>(new Vec2(), normalAttackDuration).setAnimateCallback(animate -> normalAttacking = false));
    }
}
