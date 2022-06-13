package work.chiro.game.logic.attributes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import work.chiro.game.logic.attributes.loadable.BasicAttackAttributes;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.logic.attributes.loadable.LoadableAttributes;
import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.ResourceProvider;

public class AttributesBuilder {
    private static final Map<Class<?>, String> resourceTypeDirectoryMap = Map.of(
            BasicCharacterAttributes.class, "characters",
            BasicAttackAttributes.class, "attacks"
    );

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
