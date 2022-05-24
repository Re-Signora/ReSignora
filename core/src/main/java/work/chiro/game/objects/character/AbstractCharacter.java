package work.chiro.game.objects.character;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public abstract class AbstractCharacter extends AbstractFlyingObject {
    final private String name;


    public AbstractCharacter(String name, Vec2 posInit, AnimateContainer animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(posInit, animateContainer, sizeInit, rotationInit, alpha);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
