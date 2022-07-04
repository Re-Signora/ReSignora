package work.chiro.game.x.ui.layout;

import java.util.HashMap;
import java.util.Map;

import work.chiro.game.x.ui.view.XView;

public class XLayoutManager {
    // static private XLayoutManager instance = null;
    private final Map<String, XLayout> layouts = new HashMap<>();
    private final Map<String, XView> viewIDMap = new HashMap<>();

    // public static XLayoutManager getInstance() {
    //     if (instance == null) {
    //         synchronized (XLayoutManager.class) {
    //             instance = new XLayoutManager();
    //         }
    //     }
    //     return instance;
    // }

    private XLayout buildNewLayout(String name) {
//        名字是啥啊？？？？，这个layout又是个什么玩意儿，呜呜,搜不到调用？？？？什么鬼啊，但是又不是灰色的，什么xp
//        在哪儿有调用的啊qwq，没找到调用呢，name是从哪里来的啊>w<
        XLayout layout = new XLayoutBuilder(this, name).build();
        layouts.put(name, layout);
        return layout;
    }

    public XLayoutManager() {
    }

    public Map<String, XView> getViewIDMap() {
        return viewIDMap;
    }

    public XLayout getLayout(String name) {
        XLayout res = layouts.get(name);
//        是从这里出来的？？？？，前面那个res是判空是嘛，为什么叠了那么多层啊
        if (res == null) {
            return buildNewLayout(name);
        }
        return res;
    }

    public XView getViewByID(String id) {
        return viewIDMap.getOrDefault(id, null);
    }

    public void putViewByID(String id, XView view) {
        viewIDMap.put(id, view);
    }
}
