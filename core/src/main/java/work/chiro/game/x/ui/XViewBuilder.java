package work.chiro.game.x.ui;

import work.chiro.game.vector.Vec2;

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
            case View:
            default:
                view = new XView(posInit);
                break;

        }
        setup(view);
        return view;
    }
}
