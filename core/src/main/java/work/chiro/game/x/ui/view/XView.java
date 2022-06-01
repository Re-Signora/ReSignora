package work.chiro.game.x.ui.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.config.Constants;
import work.chiro.game.objects.AbstractObject;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;
import work.chiro.game.x.ui.builder.XViewCallback;
import work.chiro.game.x.ui.event.XEvent;
import work.chiro.game.x.ui.event.XEventType;

public class XView extends AbstractObject<AnimateContainer> {
    protected String text = "";
    protected String font = "main";
    protected String id = "View" + Utils.idGenerator();
    private final Map<XEventType, XViewCallback> listeners = new HashMap<>();
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
        return addListener(XEventType.Click, callback);
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
        super.draw(g, false);
        g.drawUIString(this, getText());
    }

    @Override
    public void vanish() {
        // 不会消失
    }

    private final static Map<String, XViewType> viewTypeStringMap = Map.of(
            "View", XViewType.View,
            "Button", XViewType.Button,
            "JoySticks", XViewType.JoySticks
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
}
