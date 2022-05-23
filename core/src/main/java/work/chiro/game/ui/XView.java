package work.chiro.game.ui;

import java.util.HashMap;
import java.util.Map;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.basic.AbstractObject;
import work.chiro.game.compatible.XGraphics;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

public class XView extends AbstractObject {
    protected String text = "";
    protected String id = "View" + Utils.idGenerator();
    private final Map<XEventType, XViewCallback> listeners = new HashMap<>();
    protected String imageResource = null;
    protected XViewType type;

    public XView(Vec2 posInit, Vec2 sizeInit) {
        super(posInit, new AnimateContainer(), sizeInit);
    }

    public XView(Vec2 posInit) {
        super(posInit, new AnimateContainer());
    }

    public XView setImageResource(String imageResource) {
        this.imageResource = imageResource;
        return this;
    }

    public XView setText(String text) {
        this.text = text;
        return this;
    }

    public XView setId(String id) {
        this.id = id;
        return this;
    }

    public XView setType(XViewType viewType) {
        this.type = viewType;
        return this;
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

    @Override
    protected String getImageFilename() {
        if (imageResource == null)
            return super.getImageFilename();
        else return imageResource;
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g, false);
    }

    @Override
    public void vanish() {
        // 不会消失
    }

    private final static Map<String, XViewType> viewTypeStringMap = Map.of(
            "View", XViewType.View,
            "Button", XViewType.Button
    );

    public static XViewType stringToType(String type) {
        return viewTypeStringMap.get(type);
    }
}
