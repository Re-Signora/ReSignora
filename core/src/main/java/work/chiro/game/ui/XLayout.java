package work.chiro.game.ui;

import java.util.ArrayList;
import java.util.List;

import work.chiro.game.compatible.XGraphics;

public abstract class XLayout {
    private final List<XView> viewList = new ArrayList<>();

    /**
     * 绘制当前容器内所有 UI 物体
     * @param g XGraphics
     * @return this
     */
    public XLayout show(XGraphics g) {
        viewList.forEach(view -> view.draw(g));
        return this;
    }

    /**
     * 触发一个事件
     * @param event 事件
     * @return this
     */
    public XLayout trigger(XEvent event) {
        viewList.forEach(view -> view.trigger(event));
        return this;
    }

    /**
     * 添加一个控件
     * @param view 控件
     * @return this
     */
    public XLayout addView(XView view) {
        viewList.add(view);
        return this;
    }

    /**
     * 清除所有控件
     * @return this
     */
    public XLayout clearView() {
        viewList.clear();
        return this;
    }
}
