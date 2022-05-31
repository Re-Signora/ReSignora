package work.chiro.game.objects.thing.character;

import java.util.List;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.logic.attributes.BasicCharacterAttributes;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.objects.thing.attack.AbstractAttack;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class AbstractCharacter extends AbstractThing<BasicCharacterAttributes, AbstractAction> {
    public AbstractCharacter(String labelName, Class<BasicCharacterAttributes> attributesClass, Vec2 posInit, AbstractAction animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, attributesClass, posInit, animateContainer, sizeInit, rotationInit, alpha);
    }

    public AbstractCharacter(String labelName, Class<BasicCharacterAttributes> attributesClass, Vec2 posInit, AbstractAction animateContainer) {
        this(labelName, attributesClass, posInit, animateContainer, null, null, null);
    }

    public List<AbstractAttack> normalAttack() {
        return List.of();
    }

    public List<AbstractAttack> skillAttack() {
        return List.of();
    }

    public List<AbstractAttack> chargedAttack() {
        return List.of();
    }
}
