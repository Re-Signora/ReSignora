package work.chiro.game.compatible;

import java.io.IOException;

public abstract class ResourceProvider {
    static private ResourceProvider instance = null;

    public static ResourceProvider getInstance() {
        assert instance != null;
        return instance;
    }

    public static void setInstance(ResourceProvider instance) {
        ResourceProvider.instance = instance;
    }

    public abstract XImage<?> getImageFromResource(String path) throws IOException;

    public abstract byte[] getSoundBytesFromResource(String path) throws IOException;
}
