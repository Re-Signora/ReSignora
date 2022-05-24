package work.chiro.game.objects.character;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public abstract class AbstractCharacter extends AbstractFlyingObject {
    public AbstractCharacter(Vec2 posInit, AnimateContainer animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(posInit, animateContainer, sizeInit, rotationInit, alpha);
    }
}
