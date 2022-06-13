package work.chiro.game.objects.thing.character.shogunate;

import java.io.IOException;

import work.chiro.game.animate.Animate;
import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.game.Game;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.DelayTimer;
import work.chiro.game.utils.timer.Timer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XImage;

public class ShogunateSoldier extends AbstractCharacter {
    private boolean normalAttacking = false;
    private int normalAttackDuration = 1000;
    private Timer normalAttackTask = null;

    public ShogunateSoldier(Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("shogunate-soldier", BasicCharacterAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
        normalAttackTask = new Timer(getBasicAttributes().getNormalAttackCoolDown() * 1000, (controller, timer) -> this.normalAttack());
        Game.getInstance().getTimerController().add(getClass(), normalAttackTask);
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
        if (normalAttacking) {
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

    @Override
    public void vanish() {
        super.vanish();
        Game.getInstance().getTimerController().remove(getClass(), normalAttackTask);
    }
}
