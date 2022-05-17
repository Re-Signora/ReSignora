package work.chiro.game.compatible;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;

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

    public byte[] getSoundBytesFromResource(String path) throws IOException {
        InputStream fileInputStream = Utils.class.getResourceAsStream("/sounds/" + path);
        if (fileInputStream == null) {
            throw new IOException("file: " + path + " not found!");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final int maxSize = 1024;
        int len;
        byte[] b = new byte[maxSize];
        while ((len = fileInputStream.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public abstract void musicLoadAll();

    public void startMusic(MusicType type) {
        startMusic(type, false);
    }

    public abstract void startMusic(MusicType type, Boolean noStop);

    public abstract void stopMusic(MusicType type);

    public abstract void stopAllMusic();

    public abstract void startLoopMusic(MusicType type);
}
