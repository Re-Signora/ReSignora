package work.chiro.game.compatible;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import work.chiro.game.logger.AbstractLogger;
import work.chiro.game.logger.BasicLogger;
import work.chiro.game.resource.MusicType;
import work.chiro.game.ui.XLayoutBean;
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

    public XLayoutBean getLayoutBeanFromResource(String name) throws IOException {
        InputStream fileInputStream = Utils.class.getResourceAsStream("/layout/" + name + ".json");
        if (fileInputStream == null) {
            throw new IOException("file: " + name + ".json not found!");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final int maxSize = 1024;
        int len;
        byte[] b = new byte[maxSize];
        while ((len = fileInputStream.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, len);
        }
        String jsonString = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        return new Gson().fromJson(jsonString, XLayoutBean.class);
    }

    public abstract void musicLoadAll();

    public void startMusic(MusicType type) {
        startMusic(type, false);
    }

    public abstract void startMusic(MusicType type, Boolean noStop);

    public abstract void stopMusic(MusicType type);

    public abstract void stopAllMusic();

    public abstract void startLoopMusic(MusicType type);

    private static final BasicLogger basicLogger = new BasicLogger();

    public AbstractLogger getLogger() {
        return basicLogger;
    }
}
