package work.chiro.game.x.ui.view;

import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.DrawColor;
import work.chiro.game.x.compatible.XGraphics;

public class XJoySticks extends XView {
    private Vec2 pointer = null;

    public XJoySticks(Vec2 posInit) {
        super(posInit);
        setOnMove((xView, xEvent) -> pointer = xEvent.getPosition());
        setOnLeft((xView, xEvent) -> pointer = null);
        setOnUp((xView, xEvent) -> pointer = null);
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
        g.setColor(DrawColor.green);
        g.circle(getLocationX() + getWidth() / 2, getLocationY() + getHeight() / 2, Math.min(getWidth(), getHeight()) / 2);
        if (pointer != null) {
            g.setColor(DrawColor.gray);
            g.circle(getLocationX() + pointer.getX(), getLocationY() + pointer.getY(), 50);
        }
    }
}
