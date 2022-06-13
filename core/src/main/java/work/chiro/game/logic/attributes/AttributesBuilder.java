package work.chiro.game.logic.attributes;

import java.io.IOException;
import java.util.Map;

import work.chiro.game.logic.attributes.loadable.BasicAttackAttributes;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.x.compatible.ResourceProvider;

public class AttributesBuilder {
    private static final Map<Class<?>, String> resourceTypeDirectoryMap = Map.of(
            BasicCharacterAttributes.class, "characters",
            BasicAttackAttributes.class, "attacks"
    );
    public static <T extends BasicThingAttributes> T buildFromResource(String name, Class<T> clazz) throws IOException {
        return ResourceProvider.getInstance().getAttributesFromResource(name, clazz, resourceTypeDirectoryMap.getOrDefault(clazz, "others"));
    }
}
