package work.chiro.game.objects.thing.attack;

import work.chiro.game.animate.action.BasicImageCarouselAction;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XImage;

public class Butterfly extends AbstractAttack {
    private final static String labelName = "la-signora-butterfly";
    public Butterfly(Vec2 posInit, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, posInit, new BasicImageCarouselAction("attacks/" + labelName, labelName, 100), sizeInit, rotationInit, alpha);
        getAnimateContainer().setThing(this);
    }

    public Butterfly(Vec2 posInit) {
        this(posInit, null, null, null);
    }

    @Override
    public XImage<?> getImage() {
        return ((BasicImageCarouselAction) getAnimateContainer()).getImageNow();
    }

    @Override
    public void forward() {
        getAnimateContainer().updateAll(Utils.getTimeMills());
    }
}
