package work.chiro.game.objects.thing.character.signora;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.logic.attributes.BasicCharacterAttributes;
import work.chiro.game.objects.thing.character.AbstractThing;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class LaSignora extends AbstractThing<BasicCharacterAttributes> {
    public LaSignora(Vec2 posInit, AnimateContainer animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("la-signora", BasicCharacterAttributes.class, posInit, animateContainer, sizeInit, rotationInit, alpha);
    }

    @Override
    public String getDisplayName() {
        return "女士";
    }
}
