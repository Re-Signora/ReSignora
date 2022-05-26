package work.chiro.game.objects.thing.attack;

import work.chiro.game.animate.action.BasicImageCarouselAction;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XImage;

public class Butterfly extends AbstractAttack {
    public Butterfly(Vec2 posInit, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("la-signora-butterfly", posInit, new BasicImageCarouselAction(null, "la-signora/", 100), sizeInit, rotationInit, alpha);
        getAnimateContainer().setThing(this);
    }

    @Override
    public XImage<?> getImage() {
        return ((BasicImageCarouselAction) getAnimateContainer()).getImageNow();
    }
}
