package work.chiro.game.x.ui.view.popup;

import work.chiro.game.logic.element.ElementUtils;
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
}
