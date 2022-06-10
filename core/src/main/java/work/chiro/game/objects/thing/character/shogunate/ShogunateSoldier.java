package work.chiro.game.objects.thing.character.shogunate;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.logic.attributes.BasicCharacterAttributes;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class ShogunateSoldier extends AbstractCharacter {
    public ShogunateSoldier(Vec2 posInit, AbstractAction abstractAction, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super("shogunate-soldier", BasicCharacterAttributes.class, posInit, abstractAction, sizeInit, rotationInit, alpha);
    }

    public ShogunateSoldier(Vec2 posInit, AbstractAction abstractAction) {
        this(posInit, abstractAction, null, null, null);
    }

    public ShogunateSoldier() {
        super();
    }
}
