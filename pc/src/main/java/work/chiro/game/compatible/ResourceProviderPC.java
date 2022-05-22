package work.chiro.game.compatible;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.music.MusicManagerPC;
import work.chiro.game.music.MusicThreadFactory;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;

public class ResourceProviderPC extends ResourceProvider {
    @Override
    public XImage<?> getImageFromResource(String path) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(Utils.class.getResourceAsStream("/images/" + path)));
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
}