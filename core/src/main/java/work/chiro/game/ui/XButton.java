package work.chiro.game.ui;

import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

public class XButton extends XView {
    public XButton(Vec2 posInit) {
        super(posInit);
        setOnEnter((xView, xEvent) -> Utils.getLogger().info("onEnter! {}, {}", xView, xEvent));
    }
}
