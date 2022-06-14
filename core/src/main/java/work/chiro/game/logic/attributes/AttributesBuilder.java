package work.chiro.game.logic.attributes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import work.chiro.game.logic.attributes.loadable.BasicAttackAttributes;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.logic.attributes.loadable.LoadableAttributes;
import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.ResourceProvider;

/**
 * 用于从文件中加载属性的静态类
 */
public class AttributesBuilder {
    private static final Map<Class<?>, String> resourceTypeDirectoryMap = Map.of(
            BasicCharacterAttributes.class, "characters",
            BasicAttackAttributes.class, "attacks"
    );

    /**
     * 从资源文件中构造一个属性
     * @param name 资源 labelName
     * @param clazz 需要构造的属性 Class
     * @param <T> 需要构造的属性类型
     * @return 构造的属性
     * @throws IOException 找不到文件、解码错误等
     */
    public static <T extends BasicThingAttributes> T buildFromResource(String name, Class<T> clazz) throws IOException {
        if (LoadableAttributes.class.isAssignableFrom(clazz))
            return ResourceProvider.getInstance().getAttributesFromResource(name, clazz, resourceTypeDirectoryMap.getOrDefault(clazz, "others"));
        else {
            Utils.getLogger().info("ignore {} loading", name);
            try {
                return clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
