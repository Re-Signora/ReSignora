package work.chiro.game.x.ui;

import work.chiro.game.x.ui.view.XView;

public interface XViewCallback {
    void onEvent(XView xView, XEvent xEvent);
}
