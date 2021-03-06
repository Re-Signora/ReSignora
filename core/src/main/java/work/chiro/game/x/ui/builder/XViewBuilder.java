package work.chiro.game.x.ui.builder;

import work.chiro.game.vector.Vec2;
import work.chiro.game.x.ui.view.XButton;
import work.chiro.game.x.ui.view.XDialogue;
import work.chiro.game.x.ui.view.XJoySticks;
import work.chiro.game.x.ui.view.XView;
import work.chiro.game.x.ui.view.XViewType;

public class XViewBuilder {
    private final Vec2 posInit;
    private final XViewType type;

    public XViewBuilder(XViewType type, Vec2 posInit) {
        this.posInit = posInit;
        this.type = type;
    }

    private void setup(XView view) {
        view.setType(type);
    }

    public XView build() {
        XView view;
        switch (type) {
            case Button:
                view = new XButton(posInit);
                break;
            case JoySticks:
                view = new XJoySticks();
                break;
            case Dialogue:
                view = new XDialogue();
                break;
            case View:
            default:
                view = new XView(posInit);
                break;
        }
        setup(view);
        return view;
    }
}
