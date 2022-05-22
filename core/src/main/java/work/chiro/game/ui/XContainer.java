package work.chiro.game.ui;

import java.util.List;

import work.chiro.game.compatible.XGraphics;

public abstract class XContainer {
    private List<XView> viewList;

    /**
     * 绘制当前容器内所有 UI 物体
     * @param g XGraphics
     * @return this
     */
    public XContainer show(XGraphics g) {
        viewList.forEach(view -> view.draw(g));
        return this;
    }

    /**
     * 触发一个事件
     * @param event 事件
     * @return this
     */
    public XContainer trigger(XEvent event) {
        viewList.forEach(view -> view.trigger(event));
        return this;
    }

    /**
     * 添加一个控件
     * @param view 控件
     * @return this
     */
    public XContainer addView(XView view) {
        viewList.add(view);
        return this;
    }

    /**
     * 清除所有控件
     * @return this
     */
    public XContainer clearView() {
        viewList.clear();
        return this;
    }
}
