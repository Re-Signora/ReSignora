package work.chiro.game.x.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class XLayoutManager {
    static private final List<String> layoutNames = List.of("main");
    static private XLayoutManager instance = null;
    private final Map<String, XLayout> layouts;
    private static final Map<String, Optional<XView>> viewIDMap = new HashMap<>();

    public static XLayoutManager getInstance() {
        if (instance == null) {
            synchronized (XLayoutManager.class) {
                instance = new XLayoutManager();
            }
        }
        return instance;
    }

    private XLayoutManager() {
        layouts = new HashMap<>();
        layoutNames.forEach(name -> {
            XLayout layout = new XLayoutBuilder(name).build();
            layouts.put(name, layout);
        });
    }

    static public Map<String, Optional<XView>> getViewIDMap() {
        return viewIDMap;
    }

    public XLayout getLayout(String name) {
        return layouts.get(name);
    }

    public static XView getViewByID(String id) {
        Optional<XView> d = viewIDMap.get(id);
        return d.orElse(null);
    }
}
