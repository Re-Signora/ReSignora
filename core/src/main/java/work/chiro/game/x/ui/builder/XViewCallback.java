package work.chiro.game.x.ui.builder;

import work.chiro.game.x.ui.event.XEvent;
import work.chiro.game.x.ui.view.XView;

public interface XViewCallback {
    void onEvent(XView xView, XEvent xEvent);
}
