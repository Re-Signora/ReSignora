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
        double r = Math.min(getWidth(), getHeight()) / 2;
        Vec2 size = getSize();
        g.setColor(DrawColor.green);
        g.circle(getPosition().fromVector(getPosition().plus(size.divide(2))), r);
        if (pointer != null) {
            Vec2 d = new Vec2().fromVector(pointer.minus(size.divide(2)));
            double len2 = d.getScale().getX();
            g.setColor(DrawColor.gray);
            if (len2 > r * r) {
                Vec2 u = d.fromVector(d.divide(Math.sqrt(len2)).times(r).plus(size.divide(2)));
                g.circle(getPosition().plus(u), 50);
            } else {
                g.circle(getPosition().plus(pointer), 50);
            }
        }
    }
}
