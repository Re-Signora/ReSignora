package work.chiro.game.objects.thing.character.signora;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.animate.action.ReversedImageCarouselAction;
import work.chiro.game.game.Game;
import work.chiro.game.logic.attributes.BasicCharacterAttributes;
import work.chiro.game.objects.thing.attack.Butterfly;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.callback.BasicCallback;
import work.chiro.game.utils.timer.DelayTimer;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.utils.timer.Timer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;

public class LaSignora extends AbstractCharacter {
    static class HandButterfly extends Butterfly {
        private final Vec2 butterflyOffset = new Vec2(60, -56);
        private static final Vec2 butterflySize = new Vec2(24, 24);

        public HandButterfly(Vec2 posInit, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
            super(posInit, sizeInit, rotationInit, alpha);
            getAnimateContainer().removeAnimate(moveAnimate);
        }

        public HandButterfly(Vec2 posInit) {
            this(posInit, butterflySize, null, null);
        }

        @Override
        public Vec2 getPosition() {
            return super.getPosition().plus(butterflyOffset);
        }

        @Override
        public void preLoadResources(XGraphics g) {
            ((ReversedImageCarouselAction) getAnimateContainer()).preLoadResources(g, butterflySize);
        }
    }

    private final HandButterfly handButterfly;
    private Timer normalAttackTask = null;

    public LaSignora(Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("la-signora", BasicCharacterAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
        handButterfly = new HandButterfly(getPosition());
        normalAttackTask = new Timer(getBasicAttributes().getNormalAttackCoolDown() * 1000, (controller, timer) -> this.normalAttack());
        Game.getInstance().getTimerController().add(getClass(), normalAttackTask);
    }

    public LaSignora(Vec2 posInit, AbstractAction abstractAction) {
        this(posInit, abstractAction, null, null, null);
    }

    @Override
    protected DelayTimer getDelayTimer() {
        return new DelayTimer(getClass());
    }

    public LaSignora() {
        super();
        handButterfly = null;
    }

    @Override
    public String getDisplayName() {
        return "女士";
    }

    @Override
    public void draw(XGraphics g, boolean center) {
        super.draw(g, center);
        handButterfly.draw(g);
    }

    @Override
    public void forward() {
        super.forward();
        handButterfly.forward();
    }

    @Override
    public void normalAttack() {
        super.normalAttack();
        Game.getInstance().addThing(
                new Butterfly(handButterfly.getPosition().copy(), handButterfly.getSize().copy())
                        .setImageIndexNow(handButterfly.getImageIndexNow())
        );
    }

    protected void applyAction(DelayTimer delayTimer, double delayMs, BasicCallback ifValid) {
        if (delayTimer.isValid()) {
            ifValid.run();
            delayTimer.setNotValid();
            delayTimer.setTimeMark(TimeManager.getTimeMills());
            Game.getInstance().getTimerController().add(getClass(), new Timer(delayMs, delayTimer));
        }
    }

    @Override
    public void skillAttack() {
        super.skillAttack();
        applyAction(skillAttackDelayTask,
                getBasicAttributes().getSkillAttackCoolDown() * 1000,
                () -> Game.getInstance().addThing(
                        new Butterfly(handButterfly.getPosition().copy(), handButterfly.getSize().copy())
                                .setImageIndexNow(handButterfly.getImageIndexNow())
                ));
    }

    @Override
    public void chargedAttack() {
        super.chargedAttack();
        applyAction(chargedAttackDelayTask,
                getBasicAttributes().getChargedAttackCoolDown() * 1000,
                () -> Game.getInstance().addThing(
                        new Butterfly(handButterfly.getPosition().copy(), handButterfly.getSize().copy())
                                .setImageIndexNow(handButterfly.getImageIndexNow())
                ));
    }

    @Override
    public void vanish() {
        super.vanish();
        Game.getInstance().getTimerController().remove(getClass(), normalAttackTask);
    }

    @Override
    public void preLoadResources(XGraphics g) {
        super.preLoadResources(g);
        new HandButterfly(getPosition()).preLoadResources(g);
    }
}
