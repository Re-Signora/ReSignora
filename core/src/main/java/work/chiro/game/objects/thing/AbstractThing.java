package work.chiro.game.objects.thing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.logic.attributes.AttributesBuilder;
import work.chiro.game.logic.attributes.BasicThingAttributes;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public abstract class AbstractThing<T extends BasicThingAttributes, A extends AnimateContainer>
        extends AbstractFlyingObject<A> {
    final private String labelName;
    private BasicThingAttributes basicAttributes;
    private String imageDisplaying;

    public AbstractThing(String labelName, Class<T> attributesClass, Vec2 posInit, A animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(posInit, animateContainer, sizeInit, rotationInit, alpha);
        this.labelName = labelName;
        try {
            basicAttributes = AttributesBuilder.buildFromResource(labelName, attributesClass);
        } catch (IOException e) {
            try {
                basicAttributes = attributesClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
                basicAttributes = null;
            }
        }
        imageDisplaying = getSelfImageFilename();
    }

    public String getLabelName() {
        return labelName;
    }

    public String getDisplayName() {
        return getLabelName();
    }

    public BasicThingAttributes getBasicAttributes() {
        return basicAttributes;
    }

    @Override
    protected String getImageFilename() {
        return imageDisplaying;
    }

    public void setImageDisplaying(String imageDisplaying) {
        this.imageDisplaying = imageDisplaying;
    }

    public String getSelfImageFilename() {
        return "characters/" + getLabelName() + "/self.png";
    }
}
