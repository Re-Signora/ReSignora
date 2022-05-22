package work.chiro.game.ui;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

public abstract class XView extends AbstractFlyingObject {
    protected String text;

    public XView(AbstractConfig config, Vec2 posInit, AnimateContainer animateContainer, Vec2 sizeInit) {
        super(config, posInit, animateContainer, sizeInit);
    }

    public abstract Vec2 getPosition();

    public abstract XView setOnClick(XViewCallback callback);

    public void setText(String text) {
        this.text = text;
    }
}
