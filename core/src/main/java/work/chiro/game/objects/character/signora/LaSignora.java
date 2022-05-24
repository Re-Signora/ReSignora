package work.chiro.game.objects.character.signora;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.character.AbstractCharacter;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class LaSignora extends AbstractCharacter {
    public LaSignora(Vec2 posInit, AnimateContainer animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("la-signora", posInit, animateContainer, sizeInit, rotationInit, alpha);
    }

    @Override
    public String getDisplayName() {
        return "女士";
    }
}
