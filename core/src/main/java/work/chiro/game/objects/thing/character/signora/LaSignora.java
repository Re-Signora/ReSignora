package work.chiro.game.objects.thing.character.signora;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.animate.action.ReversedImageCarouselAction;
import work.chiro.game.game.Game;
import work.chiro.game.logic.attributes.BasicCharacterAttributes;
import work.chiro.game.objects.thing.attack.Butterfly;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.timer.Timer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;

public class LaSignora extends AbstractCharacter {
    static class HandButterfly extends Butterfly {
        private final Vec2 butterflyOffset = new Vec2(300, -280);
        private static final Vec2 butterflySize = new Vec2(120, 120);

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
    private final Timer normalAttackTask = new Timer(3000, this::normalAttack);

    public LaSignora(Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("la-signora", BasicCharacterAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
        handButterfly = new HandButterfly(getPosition());
        Game.getInstance().getTimerController().add(normalAttackTask);
    }

    public LaSignora(Vec2 posInit, AbstractAction abstractAction) {
        this(posInit, abstractAction, null, null, null);
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
                new Butterfly(handButterfly.getPosition().copy(), new Vec2(120, 120))
                        .setImageIndexNow(handButterfly.getImageIndexNow())
        );
    }

    @Override
    public void vanish() {
        super.vanish();
        Game.getInstance().getTimerController().remove(normalAttackTask);
    }

    @Override
    public void preLoadResources(XGraphics g) {
        super.preLoadResources(g);
        new HandButterfly(getPosition()).preLoadResources(g);
    }
}
