package work.chiro.game.x.ui.view;

import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public class XButton extends XView {
    public XButton(Vec2 posInit) {
        super(posInit);
        setOnEnter((xView, xEvent) -> {
            Utils.getLogger().debug("button enter");
            setAlpha(new Scale(0.6));
        });
        setOnDown((xView, xEvent) -> {
            Utils.getLogger().debug("button down");
            setAlpha(new Scale(0.6));
        });
        setOnLeft((xView, xEvent) -> {
            Utils.getLogger().debug("button left");
            setAlpha(new Scale(1));
        });
        setOnUp((xView, xEvent) -> {
            Utils.getLogger().debug("button up");
            setAlpha(new Scale(1));
        });
    }
}
