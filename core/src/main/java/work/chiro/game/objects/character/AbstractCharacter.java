package work.chiro.game.objects.character;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.logic.attributes.AttributesBuilder;
import work.chiro.game.logic.attributes.CharacterBasicAttributes;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public abstract class AbstractCharacter extends AbstractFlyingObject {
    final private String labelName;
    final private CharacterBasicAttributes basicAttributes;


    public AbstractCharacter(String labelName, Vec2 posInit, AnimateContainer animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(posInit, animateContainer, sizeInit, rotationInit, alpha);
        this.labelName = labelName;
        basicAttributes = AttributesBuilder.buildFromResource(labelName, CharacterBasicAttributes.class);
    }

    public String getLabelName() {
        return labelName;
    }

    public String getDisplayName() {
        return getLabelName();
    }

    public CharacterBasicAttributes getBasicAttributes() {
        return basicAttributes;
    }
}
