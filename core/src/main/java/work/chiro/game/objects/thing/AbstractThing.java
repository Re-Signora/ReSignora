package work.chiro.game.objects.thing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.logic.attributes.AttributesBuilder;
import work.chiro.game.logic.attributes.BasicThingAttributes;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.resource.CanPreLoadResources;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;

public abstract class AbstractThing<T extends BasicThingAttributes, A extends AnimateContainer>
        extends AbstractFlyingObject<A>
        implements CanPreLoadResources {
    final private String labelName;
    private BasicThingAttributes basicAttributes;
    private String imageDisplaying;
    protected double timeStart = 0;

    public AbstractThing(String labelName, Class<T> attributesClass, Vec2 posInit, A animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(posInit, animateContainer, sizeInit, rotationInit, alpha);
        this.labelName = labelName;
        try {
            basicAttributes = AttributesBuilder.buildFromResource(labelName, attributesClass);
        } catch (IOException e) {
            Utils.getLogger().warn("{} load attributes failed! {}", labelName, e);
            try {
                basicAttributes = attributesClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
                basicAttributes = null;
            }
        }
        if (getBasicAttributes() != null) {
            timeStart = TimeManager.getTimeMills();
        }
        imageDisplaying = getSelfImageFilename();
    }

    public AbstractThing() {
        super(new Vec2(), null);
        labelName = null;
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

    @Override
    protected Boolean checkInBoundary() {
        return true;
    }

    @Override
    public void preLoadResources(XGraphics g) {
        getImage();
    }

    @Override
    public void forward() {
        // 取消区域限制
        getAnimateContainer().updateAll(TimeManager.getTimeMills());
        if (getBasicAttributes().getDuration() > 0
                && timeStart > 0 &&
                TimeManager.getTimeMills() - timeStart > getBasicAttributes().getDuration() * 1000) {
            vanish();
        }
    }
}
