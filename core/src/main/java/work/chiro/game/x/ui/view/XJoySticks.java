package work.chiro.game.x.ui.view;

import java.util.Objects;

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
        if (RunningConfig.modePC) {
            vanish();
            return;
        }
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
            g.circle(stickOffset.fromVector(
                            stickOffset.plus(
                                    Objects.requireNonNull(getLimitedCenteredPointer())
                                            .plus(stickSize.divide(2)))),
                    50);
        }
    }

    private Vec2 getLimitedCenteredPointer() {
        if (pointer == null) return null;
        double r = stickSize.getX() / 2;
        Vec2 p = pointer.minus(stickOffset);
        Vec2 d = new Vec2().fromVector(p.minus(stickSize.divide(2)));
        double len2 = d.getScale().getX();
        return d.fromVector(d.divide(Math.sqrt(len2)).times(Math.sqrt(Math.min(r * r, len2))));
    }

    public Vec2 getPointedSpeed() {
        if (pointer == null) {
            return new Vec2();
        }
        return new Vec2().fromVector(Objects.requireNonNull(getLimitedCenteredPointer()).divide(stickSize.getX() / 2));
    }

    @Override
    public void vanish() {
        isValid = false;
    }
}
