package work.chiro.game.x.ui.view;

import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.DrawColor;
import work.chiro.game.x.compatible.XGraphics;

public class XJoySticks extends XView {
    public XJoySticks(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
        g.setColor(DrawColor.green);
        g.circle(getLocationX() + getWidth() / 2, getLocationY() + getHeight() / 2, Math.min(getWidth(), getHeight()) / 2);
    }
}
