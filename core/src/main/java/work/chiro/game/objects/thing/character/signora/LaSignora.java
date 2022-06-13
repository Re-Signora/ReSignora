package work.chiro.game.objects.thing.character.signora;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateVectorType;
import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.animate.action.ReversedImageCarouselAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.objects.thing.attack.Butterfly;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.callback.BasicCallback;
import work.chiro.game.utils.timer.DelayTimer;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.utils.timer.Timer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.colors.DrawColor;

public class LaSignora extends AbstractCharacter {
    static class HandButterfly extends Butterfly {
        private final Vec2 butterflyOffset = new Vec2(60, -56);
        private static final Vec2 butterflySize = new Vec2(24, 24);

        public HandButterfly(AbstractCharacter source, Vec2 posInit, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
            super(source, posInit, sizeInit, rotationInit, alpha, false);
            removeMoveAnimate();
            removeSizeAnimate();
        }

        public HandButterfly(AbstractCharacter source, Vec2 posInit) {
            this(source, posInit, butterflySize, null, null);
        }

        @Override
        public Vec2 getPosition() {
            return super.getPosition().plus(isFlipped() ? butterflyOffset.times(new Vec2(-1, 1)) : butterflyOffset);
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
        handButterfly = new HandButterfly(this, getPosition());
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
    public void draw(XGraphics g) {
        super.drawWithoutHp(g);
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
                new Butterfly(this, handButterfly.getPosition().copy(), handButterfly.getSize().copy())
                        .setImageIndexNow(handButterfly.getImageIndexNow())
                        .setEnemy(isEnemy())
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
        List<AbstractCharacter> enemies = Game.getInstance().getEnemies();
        List<AbstractCharacter> nearEnemies = enemies.stream().sorted(Comparator.comparing(item -> item.getPosition().minus(getPosition()).getScale().getX())).collect(Collectors.toList());
        if (nearEnemies.size() > 0) {
            Butterfly butterfly = new Butterfly(this, handButterfly.getPosition().copy(), handButterfly.getSize().copy())
                    .setImageIndexNow(handButterfly.getImageIndexNow());
            butterfly.removeMoveAnimate();
            butterfly.getAnimateContainer().addAnimate(new Animate.SmoothTo<>(butterfly.getPosition(), nearEnemies.get(0).getPosition(), AnimateVectorType.PositionLike, TimeManager.getTimeMills(), 3000));
            applyAction(skillAttackDelayTask,
                    getBasicAttributes().getSkillAttackCoolDown() * 1000,
                    () -> Game.getInstance().addThing(butterfly.setEnemy(isEnemy())));
        }
    }

    @Override
    public void chargedAttack() {
        super.chargedAttack();
        applyAction(chargedAttackDelayTask,
                getBasicAttributes().getChargedAttackCoolDown() * 1000,
                () -> Game.getInstance().addThing(
                        new Butterfly(this, handButterfly.getPosition().copy(), handButterfly.getSize().copy())
                                .setImageIndexNow(handButterfly.getImageIndexNow())
                                .setEnemy(isEnemy())
                ));
    }

    @Override
    public void preLoadResources(XGraphics g) {
        super.preLoadResources(g);
        new HandButterfly(null, getPosition()).preLoadResources(g);
        new Butterfly(null, getPosition()).preLoadResources(g);
    }

    @Override
    public void drawHp(XGraphics g, Vec2 pos, Vec2 size) {
        synchronized (LaSignora.class) {
            RunningConfig.drawHpBar *= 3;
            if (!isEnemy()) drawHp(g, DrawColor.green, DrawColor.gray, pos, size, true);
            else drawHp(g, DrawColor.orange, DrawColor.gray, pos, size, true);
            RunningConfig.drawHpBar /= 3;
        }
    }

    @Override
    public void setFlipped(boolean flipped) {
        super.setFlipped(flipped);
        handButterfly.setFlipped(flipped);
    }
}
