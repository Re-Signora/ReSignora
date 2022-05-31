package work.chiro.game.x.ui.view;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.DrawColor;
import work.chiro.game.x.compatible.XGraphics;

public class XJoySticks extends XView {
    private Vec2 pointer = null;
    private final Vec2 stickSize = new Vec2(500, 500);
    private final Vec2 stickOffset = new Vec2(200, 500);

    public XJoySticks() {
        super(new Vec2());
        setSize(new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight));
        setOnMove((xView, xEvent) -> pointer = xEvent.getPosition());
        setOnLeft((xView, xEvent) -> pointer = null);
        setOnUp((xView, xEvent) -> pointer = null);
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
        double r = stickSize.getX() / 2;
        g.setColor(DrawColor.green);
        g.circle(stickOffset.fromVector(stickOffset.plus(stickSize.divide(2))), r);
        if (pointer != null) {
            Vec2 p = pointer.minus(stickOffset);
            Vec2 d = new Vec2().fromVector(p.minus(stickSize.divide(2)));
            double len2 = d.getScale().getX();
            g.setColor(DrawColor.gray);
            if (len2 > r * r) {
                Vec2 u = d.fromVector(d.divide(Math.sqrt(len2)).times(r).plus(stickSize.divide(2)));
                g.circle(stickOffset.plus(u), 50);
            } else {
                g.circle(stickOffset.plus(p), 50);
            }
        }
    }
}
