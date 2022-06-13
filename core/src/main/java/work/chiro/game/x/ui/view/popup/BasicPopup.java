package work.chiro.game.x.ui.view.popup;

import work.chiro.game.animate.AbstractAnimate;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.logic.element.ElementUtils;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.colors.DrawColor;
import work.chiro.game.x.ui.view.XView;

public class BasicPopup extends XView {
    public Enum<?> type;

    public BasicPopup(Vec2 posInit, Enum<?> type, String text) {
        super(posInit);
        this.type = type;
        if (text != null && text.length() > 0) {
            setText(text);
        }
        setId(getClass() + "-" + (text == null ? "null" : text));
        setFont("genshin");
        setSize(new Vec2(10, 10));
        int duration = 1000;
        AbstractAnimate<Vec> upAnimate = new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeedToTarget, getPosition())
                .setupSpeed(0.2)
                .setupTarget(getPosition().minus(new Vec2(0, 100)))
                .createAnimate();
        upAnimate.setAnimateCallback(animate -> vanish());
        // AbstractAnimate<Vec> alphaAnimate = new Animate.LinearToTarget<>(getAlpha(), new Scale(1.0), 0.001, TimeManager.getTimeMills());
        getAnimateContainer().addAnimate(upAnimate);
        // getAnimateContainer().addAnimate(alphaAnimate);
    }

    public BasicPopup(Vec2 posInit, Enum<?> type) {
        this(posInit, type, "");
    }

    public Enum<?> getType() {
        return type;
    }

    public void setType(Enum<?> type) {
        this.type = type;
    }

    public String getText() {
        if (super.getText() == null || (super.getText() != null && super.getText().length() > 0)) {
            return super.getText();
        } else {
            return ElementUtils.elementToString(getType());
        }
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g, false);
        if (getText() != null && getText().length() > 0) {
            g.applyCoupleColor(DrawColor.getEnumColors(getType()));
            g.drawUIString(this, getText());
        }
    }

    @Override
    public void vanish() {
        // 会消失
        Utils.getLogger().warn("View vanish!");
        isValid = false;
        if (onVanish != null) {
            onVanish.run();
        }
    }
}
