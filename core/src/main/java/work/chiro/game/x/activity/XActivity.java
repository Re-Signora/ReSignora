package work.chiro.game.x.activity;

import work.chiro.game.game.Game;
import work.chiro.game.x.ui.XLayout;
import work.chiro.game.x.ui.XView;

public abstract class XActivity {
    final protected XLayout layout = new XLayout();
    final protected Game game;

    public XActivity(Game game) {
        this.game = game;
    }

    final protected <T extends XActivity> void startActivity(Class<T> clazz, XBundle bundle) {
        game.getActivityManager().startActivityWithBundle(clazz, bundle);
    }

    final protected <T extends XActivity> void startActivity(Class<T> clazz) {
        startActivity(clazz, null);
    }

    final protected void setContentView(String layoutName) {
        layout.replaceLayout(game.getLayoutManager().getLayout(layoutName));
    }

    final protected XView findViewById(String id) {
        return game.getLayoutManager().getViewByID(id);
    }

    final protected void finish() {
        game.getActivityManager().finishActivity(this);
    }

    final public XLayout getLayout() {
        return layout;
    }

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
