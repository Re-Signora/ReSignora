package work.chiro.game.compatible;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

public class ResourceProviderPC extends ResourceProvider {
    @Override
    public XImage<?> getImageFromResource(String path) throws IOException {
        Utils.getLogger().info("getImageFromResource({})", path);
        BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(Utils.class.getResourceAsStream("/images/" + path)));
        if (bufferedImage == null) {
            Utils.getLogger().warn("get image failed: {}", path);
        }
        return new XImageFactoryPC().create(bufferedImage);
    }

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

    @Override
    public XFont<?> getFont(String name) {
        if (cachedFont.containsKey(name)) return cachedFont.get(name);
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/fonts/genshin.ttf"))).deriveFont(22f);
        } catch (FontFormatException | IOException e) {
            f = new Font("SansSerif", Font.PLAIN, 22);
        }
        XFont<?> font = new XFont<>(f);
        cachedFont.put(name, font);
        return font;
    }
}
