package work.chiro.game.x.activity;

import work.chiro.game.game.Game;

public abstract class XActivity {
    final protected Game game;

    public XActivity(Game game) {
        this.game = game;
    }

    final protected <T extends XActivity> void startActivity(Class<T> clazz) {
    }

    final protected void setContentView(String layoutName) {
    }

    // ================ For inherit ================

    protected void onCreate(XBundle savedInstanceState) {
    }

    protected void onStart() {
    }

    protected void onStop() {
    }
}
