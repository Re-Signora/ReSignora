package work.chiro.game.x.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class XLayoutManager {
    private final List<String> layoutNames = List.of("main");
    static private XLayoutManager instance = null;
    private final Map<String, XLayout> layouts;
    private final Map<String, Optional<XView>> viewIDMap = new HashMap<>();

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
            XLayout layout = new XLayoutBuilder(this, name).build();
            layouts.put(name, layout);
        });
    }

    public Map<String, Optional<XView>> getViewIDMap() {
        return viewIDMap;
    }

    public XLayout getLayout(String name) {
        return layouts.get(name);
    }

    public XView getViewByID(String id) {
        Optional<XView> d = viewIDMap.get(id);
        return d.orElse(null);
    }

    public void putViewByID(String id, XView view) {
        viewIDMap.put(id, Optional.of(view));
    }
}
