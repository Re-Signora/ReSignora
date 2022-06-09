package work.chiro.game.x.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import work.chiro.game.game.Game;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.ui.event.XEvent;
import work.chiro.game.x.ui.event.XEventType;
import work.chiro.game.x.ui.layout.XLayout;
import work.chiro.game.x.ui.view.XView;

@SuppressWarnings("SameParameterValue")
public abstract class XActivity {
    final protected XLayout layout = new XLayout();
    final protected Game game;

    public XActivity(Game game) {
        this.game = game;
    }

    /**
     * 用一个 clazz 启动一个 xActivity，并提供一个 xBundle 参数
     *
     * @param clazz  目标 clazz
     * @param bundle 参数
     * @param <T>    Class[T]
     */
    final protected <T extends XActivity> void startActivity(Class<T> clazz, XBundle bundle) {
        game.getActivityManager().startActivityWithBundle(clazz, bundle);
    }

    /**
     * 用一个 clazz 启动一个 xActivity，伴随空参数
     *
     * @param clazz 目标 clazz
     * @param <T>   Class[T]
     */
    final protected <T extends XActivity> void startActivity(Class<T> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 设置当前 xLayout，在 onCreate 中调用
     *
     * @param layoutName xLayout 名称
     */
    final protected void setContentView(String layoutName) {
        layout.replaceLayout(game.getLayoutManager().getLayout(layoutName));
    }

    /**
     * 在 **所有** id 中查找一个 xView
     *
     * @param id id
     * @return xView
     */
    final protected XView findViewById(String id) {
        return game.getLayoutManager().getViewByID(id);
    }

    /**
     * 结束当前 xActivity
     */
    final protected void finish() {
        game.getActivityManager().finishActivity(this);
    }

    /**
     * 得到 xLayout
     *
     * @return xLayout
     */
    final public XLayout getLayout() {
        return layout;
    }

    /**
     * 全局的 Game 对象
     *
     * @return game
     */
    final public Game getGame() {
        return game;
    }

    public void actionPointerPressed(Vec2 pos) {
        actionPointerDragged(List.of(pos));
    }

    public void actionPointerDragged(List<Vec2> posList) {
        Map<XView, Integer> enteredMark = new HashMap<>();
        for (int i = 0; i < posList.size(); i++) {
            Vec2 pos = posList.get(i);
            for (int j = layout.size() - 1; j >= 0; j--) {
                XView view = layout.get(j);
                view.setWillStopTrigger(false);
                if (enteredMark.containsKey(view)) return;
                Vec2 relativePosition = pos.minus(view.getPosition());
                if (view.isIn(relativePosition)) {
                    enteredMark.put(view, i);
                    if (!view.isEntered()) {
                        view.trigger(new XEvent(XEventType.Down)
                                .setPosition(relativePosition));
                        view.setEntered(true);
                    }
                    view.trigger(new XEvent(XEventType.Move)
                            .setPosition(relativePosition));
                } else {
                    if (view.isEntered()) {
                        view.trigger(new XEvent(XEventType.Up)
                                .setPosition(relativePosition));
                        view.setEntered(false);
                    }
                }
                if (view.getWillStopTrigger()) break;
            }
        }
    }

    public void actionPointerRelease(Vec2 pos) {
        for (int j = layout.size() - 1; j >= 0; j--) {
            XView view = layout.get(j);
            view.setWillStopTrigger(false);
            Vec2 relativePosition = pos.minus(view.getPosition());
            if (view.isIn(relativePosition)) {
                view.trigger(new XEvent(XEventType.Move)
                        .setPosition(relativePosition));
                    view.trigger(new XEvent(XEventType.Click)
                            .setPosition(relativePosition));
                if (view.isEntered()) {
                    view.trigger(new XEvent(XEventType.Up)
                            .setPosition(relativePosition));
                    view.setEntered(false);
                } else {
                    Utils.getLogger().info("not in: {}", view);
                }
            }
            if (view.getWillStopTrigger()) break;
        }
    }

    // ================ For inherit ================

    protected void onCreate(XBundle savedInstanceState) {
    }

    protected void onStart() {
    }

    protected void onStop() {
    }
}
