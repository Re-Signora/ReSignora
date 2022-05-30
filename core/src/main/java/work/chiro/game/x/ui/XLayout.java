package work.chiro.game.x.ui;

import java.util.concurrent.CopyOnWriteArrayList;

import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.ui.view.XView;

public class XLayout extends CopyOnWriteArrayList<XView> {
    private String name;
    private String background = null;

    public String getName() {
        return name;
    }

    public XLayout(String name) {
        this.name = name;
    }

    public XLayout() {
        this(null);
    }

    public XLayout setBackground(String background) {
        this.background = background;
        return this;
    }

    public String getBackground() {
        return background;
    }

    /**
     * 替换内容
     *
     * @param that 另一个 XLayout 对象
     * @return this
     */
    public XLayout replaceLayout(XLayout that) {
        clear();
        addAll(that);
        this.name = that.name;
        this.background = that.background;
        return this;
    }

    /**
     * 绘制当前容器内所有 UI 物体
     *
     * @param g XGraphics
     * @return this
     */
    public XLayout show(XGraphics g) {
        this.forEach(view -> view.draw(g));
        return this;
    }

    /**
     * 触发一个事件
     *
     * @param event 事件
     * @return this
     */
    public XLayout trigger(XEvent event) {
        this.forEach(view -> view.trigger(event));
        return this;
    }

    /**
     * 添加一个控件
     *
     * @param view 控件
     * @return this
     */
    public XLayout addView(XView view) {
        add(view);
        return this;
    }

    /**
     * 清除所有控件
     *
     * @return this
     */
    public XLayout clearView() {
        clear();
        return this;
    }

    public void actionPointerPressed(Vec2 pos) {
        actionPointerDragged(pos);
    }

    public void actionPointerDragged(Vec2 pos) {
        forEach(view -> {
            if (view.isIn(pos.minus(view.getPosition()))) {
                if (!view.isEntered()) {
                    view.setEntered(true);
                }
            } else {
                if (view.isEntered()) {
                    view.setEntered(false);
                }
            }
        });
    }

    public void actionPointerRelease(Vec2 pos) {
        forEach(view -> {
            if (view.isIn(pos.minus(view.getPosition()))) {
                view.trigger(new XEvent(XEventType.Click));
                if (view.isEntered()) {
                    view.setEntered(false);
                }
            }
        });
    }
}
