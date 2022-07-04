package work.chiro.game.compatible;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.imageio.ImageIO;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.music.MusicManagerPC;
import work.chiro.game.music.MusicThreadFactory;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XFont;
import work.chiro.game.x.compatible.XImage;

//图片、音乐、字体
public class ResourceProviderPC extends ResourceProvider {
//    重新定义了一下 图片路径
    @Override
    public XImage<?> getImageFromResource(String path) throws IOException {
        Utils.getLogger().info("getImageFromResource({})", path);
        InputStream stream = Utils.class.getResourceAsStream("/images/" + path);
        if (stream == null) {
            Utils.getLogger().warn("get image failed: {}", path);
            throw new IOException("get image failed: " + path);
        }
        BufferedImage bufferedImage = ImageIO.read(stream);
        if (bufferedImage == null) {
            Utils.getLogger().warn("get image failed: {}", path);
        }
        return new XImageFactoryPC(path).create(bufferedImage);
    }
//  音乐加载 musicEnable应该是判断音乐是否可以播放吧
    @Override
    public void musicLoadAll() {
        if (RunningConfig.musicEnable) {
            MusicManagerPC.initAll();
        }
    }

    @Override
    public void startMusic(MusicType type, Boolean noStop) {
        if (RunningConfig.musicEnable) {
            MusicThreadFactory.getInstance().newMusicThread(type, noStop).start();
        }
    }
//  音乐停止
    @Override
    public void stopMusic(MusicType type) {
        MusicThreadFactory.getInstance().stopMusic(type);
    }

    @Override
    public void stopAllMusic() {
        MusicThreadFactory.getInstance().interruptAll();
    }

    @Override
    public void startLoopMusic(MusicType type) {
        if (RunningConfig.musicEnable) {
            MusicThreadFactory.getInstance().newLoopMusicThread(type).start();
        }
    }
//

    @Override
    public XFont<?> getFont(String name, double fontSize) {
//        HashMap检查是否有key 哦，这个是字体嘛
        if (cachedFont.containsKey(name)) return cachedFont.get(name);
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/fonts/" + name + ".ttf"))).deriveFont((float) fontSize);
        } catch (FontFormatException | IOException e) {
            f = new Font("SansSerif", Font.PLAIN, 22);
        }
        XFont<?> font = new XFont<>(f);
        cachedFont.put(name, font);
        return font;
    }
}
