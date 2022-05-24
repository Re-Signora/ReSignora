package work.chiro.game.x.activity;

import java.util.LinkedList;
import java.util.List;

import work.chiro.game.x.ui.XLayoutManager;

public class XActivityManager {
    private List<XActivity> activities = new LinkedList<>();
    private XLayoutManager layoutManager;

    public XActivityManager() {
        layoutManager = XLayoutManager.getInstance();
    }

    public XLayoutManager getLayoutManager() {
        return layoutManager;
    }
}
