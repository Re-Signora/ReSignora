package work.chiro.game.logic.attributes;

import java.io.IOException;

import work.chiro.game.x.compatible.ResourceProvider;

public class AttributesBuilder {
    public static <T extends BasicCharacterAttributes> T buildFromResource(String name, Class<T> clazz) {
        try {
            return ResourceProvider.getInstance().getAttributesFromResource(name, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
