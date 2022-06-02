package work.chiro.game.x.compatible;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import work.chiro.game.logic.attributes.BasicThingAttributes;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.callback.XGraphicsGetter;
import work.chiro.game.x.logger.AbstractLogger;
import work.chiro.game.x.logger.BasicLogger;
import work.chiro.game.x.ui.layout.XLayoutBean;

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

    final static public Map<MusicType, String> MUSIC_FILENAME_MAP = Map.of(
            MusicType.BGM, "bgm.wav",
            MusicType.BGM_BOSS, "bgm_boss.wav",
            MusicType.BOMB_EXPLOSION, "bomb_explosion.wav",
            MusicType.HERO_SHOOT, "bullet.wav",
            MusicType.HERO_HIT, "bullet_hit.wav",
            MusicType.PROPS, "get_supply.wav",
            MusicType.GAME_OVER, "game_over.wav"
    );

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
        String path = "/layout/" + name + ".json";
        return new Gson().fromJson(Utils.getStringFromResource(path), XLayoutBean.class);
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

    protected Map<String, XFont<?>> cachedFont = new HashMap<>();

    public abstract XFont<?> getFont(String name, double fontSize);

    public <T extends BasicThingAttributes> T getAttributesFromResource(String name, Class<T> clazz, String directory) throws IOException {
        String path = "config/" + directory + "/" + name + "/basic-attributes.json";
        return new Gson().fromJson(Utils.getStringFromResource(path), clazz);
    }

    protected XGraphicsGetter xGraphicsGetter = null;

    public XGraphics getXGraphics() {
        if (xGraphicsGetter == null) return  null;
        return xGraphicsGetter.get();
    }

    public void setXGraphicsGetter(XGraphicsGetter xGraphicsGetter) {
        this.xGraphicsGetter = xGraphicsGetter;
    }
}
