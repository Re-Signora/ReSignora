package work.chiro.game.objects.thing.attack;

import work.chiro.game.animate.AbstractAnimate;
import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateVectorType;
import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.animate.action.BasicImageCarouselAction;
import work.chiro.game.animate.action.ReversedImageCarouselAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;

/**
 * 女士的蝴蝶攻击
 */
public class Butterfly extends AbstractAttack {
    private final static String labelName = "la-signora-butterfly";
    /**
     * 蝴蝶移动动画
     */
    protected AbstractAnimate<Vec> moveAnimate;
    /**
     * 蝴蝶大小变换动画
     */
    protected AbstractAnimate<Vec> sizeAnimate = null;

    public static AbstractAction getAction() {
        return new ReversedImageCarouselAction("attacks/" + labelName, labelName, 200);
    }

    public Butterfly(AbstractCharacter source, Vec2 posInit, Vec2 sizeInit, Scale rotationInit, Scale alpha, boolean setSizeAnimate) {
        super(source, labelName, posInit, getAction(), sizeInit, rotationInit, alpha);
        getAnimateContainer().setThing(this);
        if (source != null)
            setEnemy(source.isEnemy());
        Utils.getLogger().warn("create butterfly is enemy: {}", isEnemy());
        moveAnimate = new Animate.SmoothTo<>(
                posInit,
                new Vec2(isEnemy() ? 0 : RunningConfig.windowWidth, posInit.getY()),
                AnimateVectorType.PositionLike,
                TimeManager.getTimeMills(),
                6000)
                .setAnimateCallback(animate -> vanish());
        getAnimateContainer().addAnimate(moveAnimate);
        if (getBasicAttributes().isSizeAvailable() && setSizeAnimate) {
            Vec2 targetSize = getBasicAttributes().getSize();
            if (RunningConfig.modePC) {
                setSize(targetSize);
                // PC 上就不用缩放动画了，怪慢的
            } else {
                if (getSize() == null) setSize(targetSize);
                // Android 平台进行缩放性能好一些
                sizeAnimate = new Animate.SmoothTo<>(
                        getSize(),
                        targetSize,
                        AnimateVectorType.Others,
                        TimeManager.getTimeMills(),
                        3000
                );
                getAnimateContainer().addAnimate(sizeAnimate);
            }
        }
    }

    public Butterfly(AbstractCharacter source, Vec2 posInit) {
        this(source, posInit, null);
    }

    public Butterfly(AbstractCharacter source, Vec2 posInit, Vec2 sizeInit) {
        this(source, posInit, sizeInit, null, null, true);
    }

    @Override
    public XImage<?> getImage(boolean getRawImage) {
        XImage<?> im = ((BasicImageCarouselAction) getAnimateContainer()).getImageNow();
        if (getRawImage) return im;
        if (RunningConfig.enableImageCache)
            return cachedImage.getOrDefault(im, im);
        else return im;
    }

    @Override
    public void forward() {
        getAnimateContainer().updateAll(TimeManager.getTimeMills());
    }

    @Override
    public void preLoadResources(XGraphics g) {
        super.preLoadResources(g);
        getAnimateContainer().preLoadResources(g);
        ((ReversedImageCarouselAction) getAnimateContainer()).preLoadResources(g, getBasicAttributes().getSize());
    }

    public Butterfly setImageIndexNow(int imageIndexNow) {
        ((BasicImageCarouselAction) getAnimateContainer()).setImageIndexNow(imageIndexNow);
        return this;
    }

    public int getImageIndexNow() {
        return ((BasicImageCarouselAction) getAnimateContainer()).getImageIndexNow();
    }

    public void removeMoveAnimate() {
        getAnimateContainer().removeAnimate(moveAnimate);
    }

    public void removeSizeAnimate() {
        getAnimateContainer().removeAnimate(sizeAnimate);
    }
}
