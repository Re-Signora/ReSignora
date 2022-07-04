package work.chiro.game.resource;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import work.chiro.game.x.compatible.XImage;
import work.chiro.game.utils.Utils;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private final Map<String, XImage<?>> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public XImage<?> HERO_IMAGE;

    private static ImageManager instance = null;

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                instance = new ImageManager();
            }
        }
        return instance;
    }

    ImageManager() {
    }

    public XImage<?> get(String className) {
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public XImage<?> get(Class<?> clazz) {
        return get(clazz.getName());
    }

}
