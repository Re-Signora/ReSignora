package work.chiro.game.x.activity;

import work.chiro.game.game.Game;
import work.chiro.game.x.ui.XLayout;
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
     * @param clazz 目标 clazz
     * @param bundle 参数
     * @param <T> Class[T]
     */
    final protected <T extends XActivity> void startActivity(Class<T> clazz, XBundle bundle) {
        game.getActivityManager().startActivityWithBundle(clazz, bundle);
    }

    /**
     * 用一个 clazz 启动一个 xActivity，伴随空参数
     * @param clazz 目标 clazz
     * @param <T> Class[T]
     */
    final protected <T extends XActivity> void startActivity(Class<T> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 设置当前 xLayout，在 onCreate 中调用
     * @param layoutName xLayout 名称
     */
    final protected void setContentView(String layoutName) {
        layout.replaceLayout(game.getLayoutManager().getLayout(layoutName));
    }

    /**
     * 在 **所有** id 中查找一个 xView
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
     * @return xLayout
     */
    final public XLayout getLayout() {
        return layout;
    }

    /**
     * 全局的 Game 对象
     * @return game
     */
    final public Game getGame() {
        return game;
    }

    // ================ For inherit ================

    protected void onCreate(XBundle savedInstanceState) {
    }

    protected void onStart() {
    }

    protected void onStop() {
    }
}
