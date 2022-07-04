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
//        ？？？这个断言是拿来干嘛的？
        return instance;
    }

    public static void setInstance(ResourceProvider instance) {
        ResourceProvider.instance = instance;
    }

    public abstract XImage<?> getImageFromResource(String path) throws IOException;


    //    音乐组件
    final static public Map<MusicType, String> MUSIC_FILENAME_MAP = Map.of(
            MusicType.BGM, "bgm.wav",
            MusicType.BGM_BOSS, "bgm_boss.wav",
            MusicType.BOMB_EXPLOSION, "bomb_explosion.wav",
            MusicType.HERO_SHOOT, "bullet.wav",
            MusicType.HERO_HIT, "bullet_hit.wav",
            MusicType.PROPS, "get_supply.wav",
            MusicType.GAME_OVER, "game_over.wav"
    );

//??? 字节缓存，音乐播放？
    public byte[] getSoundBytesFromResource(String path) throws IOException {
        InputStream fileInputStream = Utils.class.getResourceAsStream("/sounds/" + path);
        if (fileInputStream == null) {
            throw new IOException("file: " + path + " not found!");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final int maxSize = 1024;
        int len;
//        ？？？你这个len有初始化嘛qwq，直接用？
        byte[] b = new byte[maxSize];
        while ((len = fileInputStream.read(b)) != -1) {
//            等于格式转换，先读到一个BUffer再转换？！！！
            byteArrayOutputStream.write(b, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

//layout是啥？？
    public XLayoutBean getLayoutBeanFromResource(String name) throws IOException {
//        还能这样？？？？路径layout/名字/json？那为什么是.json呢？qwq
//        不是，这玩意儿是整个大地图的啊，那角色的呢qwq？不是很能理解
        String path = "/layout/" + name + ".json";
//        getStringFromResource是专门的函数？
        return new Gson().fromJson(Utils.getStringFromResource(path), XLayoutBean.class);
    }

    public abstract void musicLoadAll();
//    啊啊啊啊，我又在这两个地方反复横跳，好恐怖呜呜？？？？

//    Map能这么用诶~amazing
    public void startMusic(MusicType type) {
        startMusic(type, false);
    }


    public abstract void startMusic(MusicType type, Boolean noStop);

//    这两个start有啥区别？

    public abstract void stopMusic(MusicType type);

    public abstract void stopAllMusic();

    public abstract void startLoopMusic(MusicType type);
//  这又是啥qwq

    private static final BasicLogger basicLogger = new BasicLogger();


//    日志追踪？？？有啥用的啊
    public AbstractLogger getLogger() {
        return basicLogger;
    }

    protected Map<String, XFont<?>> cachedFont = new HashMap<>();
//  为什么没有注释啊这是什么玩意儿   字体控制

    public abstract XFont<?> getFont(String name, double fontSize);
//  我不是很理解这里继承了个什么emmm
    public <T extends BasicThingAttributes> T getAttributesFromResource(String name, Class<T> clazz, String directory) throws IOException {
        String path = "/config/" + directory + "/" + name + "/basic-attributes.json";
        return new Gson().fromJson(Utils.getStringFromResource(path), clazz);
    }
//  表格绘制？？？XGraph这一片都不是很理解emm
//    绘图！！！！
    protected XGraphicsGetter xGraphicsGetter = null;

    public XGraphics getXGraphics() {
        if (xGraphicsGetter == null) return null;
        return xGraphicsGetter.get();
    }

    public void setXGraphicsGetter(XGraphicsGetter xGraphicsGetter) {
        this.xGraphicsGetter = xGraphicsGetter;
    }
//  不是很理解qwq
    public void stopXGraphics() {
    }

    public Object waitXGraphicsObject() {
        return ResourceProvider.class;
    }
}
