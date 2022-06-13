package work.chiro.game.x.ui.view.popup;

import work.chiro.game.animate.AbstractAnimate;
import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateVectorType;
import work.chiro.game.logic.element.ElementUtils;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Scale;
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
        AbstractAnimate<Vec> upAnimate = new Animate.SmoothTo<>(getPosition(), getPosition().minus(new Vec2(0, 200)), AnimateVectorType.Others, TimeManager.getTimeMills(), duration);
        upAnimate.setAnimateCallback(animate -> vanish());
        setAlpha(new Scale(0.3));
        AbstractAnimate<Vec> alphaAnimate = new Animate.SmoothTo<>(getAlpha(), new Scale(1.0), AnimateVectorType.Others, TimeManager.getTimeMills(), duration);
        getAnimateContainer().addAnimate(upAnimate);
        getAnimateContainer().addAnimate(alphaAnimate);
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
            g.setAlpha(getAlpha().getX());
            g.drawUIString(this, getText());
        }
    }

    @Override
    public void vanish() {
        // 会消失
        isValid = false;
        if (onVanish != null) {
            onVanish.run();
        }
    }
}
