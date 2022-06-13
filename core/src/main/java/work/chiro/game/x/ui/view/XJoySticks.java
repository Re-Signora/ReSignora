package work.chiro.game.x.ui.view;

import java.util.Objects;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.colors.DrawColor;
import work.chiro.game.x.compatible.XGraphics;

public class XJoySticks extends XView {
    private Vec2 pointer = null;
    private final Vec2 stickSize = new Vec2(450, 450);
    private final static Vec2 stickViewOffset = new Vec2(0, 100);
    private final Vec2 stickOffset = new Vec2(220, 420);

    public XJoySticks() {
        super(stickViewOffset);
        setSize(new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight).minus(stickViewOffset));
        if (RunningConfig.modePC) {
            setVisible(false);
            return;
        }
        setOnMove((xView, xEvent) -> pointer = xEvent.getPosition().plus(stickViewOffset));
        setOnLeft((xView, xEvent) -> {
            pointer = null;
            Utils.getLogger().debug("joysticks: left");
        });
        setOnUp((xView, xEvent) -> {
            pointer = null;
            Utils.getLogger().debug("joysticks: up");
        });
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
        if (!visible) return;
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
}
