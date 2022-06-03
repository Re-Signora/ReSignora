package work.chiro.game.objects.thing.attack;

import work.chiro.game.animate.AbstractAnimate;
import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateVectorType;
import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.animate.action.BasicImageCarouselAction;
import work.chiro.game.animate.action.ReversedImageCarouselAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;

public class Butterfly extends AbstractAttack {
    private final static String labelName = "la-signora-butterfly";
    protected AbstractAnimate<Vec> moveAnimate;

    public static AbstractAction getAction() {
        return new ReversedImageCarouselAction("attacks/" + labelName, labelName, 200);
    }

    public Butterfly(Vec2 posInit, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, posInit, getAction(), sizeInit, rotationInit, alpha);
        getAnimateContainer().setThing(this);
        moveAnimate = new Animate.SmoothTo<>(
                posInit,
                new Vec2(RunningConfig.windowWidth, posInit.getY()),
                AnimateVectorType.PositionLike,
                TimeManager.getTimeMills(),
                6000)
                .setAnimateCallback(animate -> vanish());
        getAnimateContainer().addAnimate(moveAnimate);
    }

    public Butterfly(Vec2 posInit) {
        this(posInit, null);
    }

    public Butterfly(Vec2 posInit, Vec2 sizeInit) {
        this(posInit, sizeInit, null, null);
    }

    @Override
    public XImage<?> getImage(boolean getRawImage) {
        XImage<?> im = ((BasicImageCarouselAction) getAnimateContainer()).getImageNow();
        if (getRawImage) return im;
        return cachedImage.getOrDefault(im, im);
    }

    @Override
    public void forward() {
        getAnimateContainer().updateAll(TimeManager.getTimeMills());
    }

    @Override
    public void preLoadResources(XGraphics g) {
        super.preLoadResources(g);
        getAnimateContainer().preLoadResources(g);
    }

    public Butterfly setImageIndexNow(int imageIndexNow) {
        ((BasicImageCarouselAction) getAnimateContainer()).setImageIndexNow(imageIndexNow);
        return this;
    }

    public int getImageIndexNow() {
        return ((BasicImageCarouselAction) getAnimateContainer()).getImageIndexNow();
    }
}
