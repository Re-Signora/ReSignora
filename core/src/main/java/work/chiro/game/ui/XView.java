package work.chiro.game.ui;

import java.util.HashMap;
import java.util.Map;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.basic.AbstractObject;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

public abstract class XView extends AbstractObject {
    protected String text = "";
    protected String id = "View" + Utils.idGenerator();
    private final Map<XEventType, XViewCallback> listeners = new HashMap<>();

    public XView(Vec2 posInit, Vec2 sizeInit) {
        super(posInit, new AnimateContainer(), sizeInit);
    }

    public XView setText(String text) {
        this.text = text;
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public XView trigger(XEvent event) {
        listeners.forEach((eventType, callback) -> {
            if (eventType == event.getEventType()) callback.onEvent(this, event);
        });
        return this;
    }

    public XView addListener(XEventType eventType, XViewCallback callback) {
        listeners.put(eventType, callback);
        return this;
    }

    public XView setOnClick(XViewCallback callback) {
        return addListener(XEventType.Touch, callback);
    }
}
