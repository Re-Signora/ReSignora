package work.chiro.game.ui;

import java.util.List;

public abstract class XContainer {
    private List<XView> viewList;
    public XContainer show() {
        return this;
    }
}
