package work.chiro.game.x.ui.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.config.Constants;
import work.chiro.game.objects.AbstractObject;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;
import work.chiro.game.x.compatible.colors.DrawColor;
import work.chiro.game.x.compatible.colors.UIColors;
import work.chiro.game.x.ui.builder.XViewCallback;
import work.chiro.game.x.ui.event.XEvent;
import work.chiro.game.x.ui.event.XEventType;

public class XView extends AbstractObject<AnimateContainer> {
    protected String text = "";
    protected String font = "main";
    protected String id = "View" + Utils.idGenerator();
    protected boolean visible = true;
    private final Map<XEventType, LinkedList<XViewCallback>> listeners = new HashMap<>();
    protected String imageResource = null;
    protected XViewType type;
    private boolean isEntered = false;
    private XActivity bindingActivity = null;

    public XView(Vec2 posInit) {
        super(posInit, new AnimateContainer());
    }

    public XView setImageResource(String imageResource) {
        this.imageResource = imageResource;
        return this;
    }

    public XView setText(String text) {
        this.text = text == null ? this.text : text;
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

    public XView setFont(String font) {
        this.font = font == null ? this.font : font;
        return this;
    }

    private boolean willStopTrigger = false;

    public void stopTrigger() {
        this.willStopTrigger = true;
    }

    public boolean getWillStopTrigger() {
        return willStopTrigger;
    }

    public void setWillStopTrigger(boolean willStopTrigger) {
        this.willStopTrigger = willStopTrigger;
    }

    public XView trigger(XEvent event) {
        listeners.forEach((eventType, callbackList) -> {
            if (eventType == event.getEventType() && !callbackList.isEmpty()) callbackList.getLast().onEvent(this, event);
        });
        return this;
    }

    public XView addListener(XEventType eventType, XViewCallback callback) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).add(callback);
        } else {
            LinkedList<XViewCallback> list = new LinkedList<>();
            list.add(callback);
            listeners.put(eventType, list);
        }
        return this;
    }

    public XView setOnClick(XViewCallback callback) {
        return addListener(XEventType.Click, callback);
    }

    public XView popEvent(XEventType type) {
        if (listeners.containsKey(type)) {
            LinkedList<XViewCallback> list = listeners.get(type);
            if (!list.isEmpty()) list.removeLast();
        }
        return this;
    }

    public XView setOnEnter(XViewCallback callback) {
        return addListener(XEventType.Enter, callback);
    }

    public XView setOnLeft(XViewCallback callback) {
        return addListener(XEventType.Left, callback);
    }

    public XView setOnDown(XViewCallback callback) {
        return addListener(XEventType.Down, callback);
    }

    public XView setOnUp(XViewCallback callback) {
        return addListener(XEventType.Up, callback);
    }

    public XView setOnMove(XViewCallback callback) {
        return addListener(XEventType.Move, callback);
    }

    @Override
    protected String getImageFilename() {
        if (imageResource == null)
            return super.getImageFilename();
        else return imageResource;
    }

    @Override
    public void draw(XGraphics g) {
        if (!visible) return;
        draw(g, false);
        if (getText() != null && getText().length() > 0) {
            g.applyCoupleColor(DrawColor.getEnumColors(UIColors.Default));
            g.drawUIString(this, getText());
        }
    }

    /**
     * 默认为从左上角开始绘制。
     * @param g xGraphics
     * @param center set default to false
     */
    @Override
    final public void draw(XGraphics g, boolean center) {
        if (!visible) return;
        super.draw(g, false);
    }

    @Override
    public void vanish() {
        // 不会消失
    }

    private final static Map<String, XViewType> viewTypeStringMap = Map.of(
            "View", XViewType.View,
            "Button", XViewType.Button,
            "JoySticks", XViewType.JoySticks,
            "Dialogue", XViewType.Dialogue
    );

    public static XViewType stringToType(String type) {
        return viewTypeStringMap.get(type);
    }

    public boolean isIn(Vec2 pos) {
        XImage<?> im = getImage();
        Utils.getLogger().debug("isIn({})", pos);
        boolean useSize = false;
        if (im == null) {
            Utils.getLogger().debug("empty image when isIn({})", pos);
            useSize = true;
        } else {
            if (im.getWidth() != (int) getWidth() || im.getHeight() != (int) getHeight()) {
                useSize = true;
            }
        }
        if (useSize) {
            Utils.getLogger().debug("isIn(): use size!");
            return pos.getX() >= 0 && pos.getX() <= getWidth() && pos.getY() >= 0 && pos.getY() <= getHeight();
        } else {
            try {
                int pixel = im.getPixel(pos);
                int alpha = pixel >> 24 & 0xff;
                Utils.getLogger().debug("isIn({}) got alpha: {} pixel: {}", pos, String.format(Locale.CHINA, "0x%02x", alpha), String.format(Locale.CHINA, "0x%08x", pixel));
                return alpha > Constants.UI_CLICK_ALPHA_THRESHOLD;
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
    }

    public void setEntered(boolean entered) {
        isEntered = entered;
        if (entered) {
            trigger(new XEvent(XEventType.Enter));
        } else {
            trigger(new XEvent(XEventType.Left));
        }
    }

    public boolean isEntered() {
        return isEntered;
    }

    public XView setBindingActivity(XActivity bindingActivity) {
        this.bindingActivity = bindingActivity;
        return this;
    }

    public XActivity getBindingActivity() {
        return bindingActivity;
    }

    public String getText() {
        return text;
    }

    public String getFont() {
        return font;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void forward() {
        getAnimateContainer().updateAll(TimeManager.getTimeMills());
    }
}
