package work.chiro.game.x.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class XLayoutManager {
    static private XLayoutManager instance = null;
    private final Map<String, XLayout> layouts = new HashMap<>();
    private final Map<String, Optional<XView>> viewIDMap = new HashMap<>();

    public static XLayoutManager getInstance() {
        if (instance == null) {
            synchronized (XLayoutManager.class) {
                instance = new XLayoutManager();
            }
        }
        return instance;
    }

    private XLayout buildNewLayout(String name) {
        XLayout layout = new XLayoutBuilder(this, name).build();
        layouts.put(name, layout);
        return layout;
    }

    private XLayoutManager() {
    }

    public Map<String, Optional<XView>> getViewIDMap() {
        return viewIDMap;
    }

    public XLayout getLayout(String name) {
        XLayout res = layouts.get(name);
        if (res == null) {
            return buildNewLayout(name);
        }
        return res;
    }

    public XView getViewByID(String id) {
        Optional<XView> d = viewIDMap.get(id);
        return d.orElse(null);
    }

    public void putViewByID(String id, XView view) {
        viewIDMap.put(id, Optional.of(view));
    }
}
