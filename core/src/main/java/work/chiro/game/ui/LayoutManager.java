package work.chiro.game.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutManager {
    static private final List<String> layoutNames = List.of("main");
    static private LayoutManager instance = null;
    private final Map<String, XLayout> layouts;

    public static LayoutManager getInstance() {
        if (instance == null) {
            synchronized (LayoutManager.class) {
                instance = new LayoutManager();
            }
        }
        return instance;
    }

    private LayoutManager() {
        layouts = new HashMap<>();
        layoutNames.forEach(name -> {
            XLayout layout = new XLayoutBuilder(name).build();
            layouts.put(name, layout);
        });
    }

    public XLayout getLayout(String name) {
        return layouts.get(name);
    }
}
