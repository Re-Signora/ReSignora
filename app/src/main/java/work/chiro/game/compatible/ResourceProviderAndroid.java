package work.chiro.game.compatible;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.core.content.res.ResourcesCompat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import work.chiro.game.application.R;
import work.chiro.game.config.Constants;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XFont;
import work.chiro.game.x.compatible.XImage;
import work.chiro.game.x.logger.AbstractLogger;

public abstract class ResourceProviderAndroid extends ResourceProvider {
    @Override
    public XImage<?> getImageFromResource(String path) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(Utils.class.getResourceAsStream("/images/" + path));
        if (bitmap == null) {
            throw new IOException("file: " + path + " not found!");
        }
        return new XImageFactoryAndroid().create(bitmap);
    }

    private final Map<MusicType, Integer> musicResource = Map.of(
            MusicType.BGM, R.raw.bgm,
            MusicType.BGM_BOSS, R.raw.bgm_boss,
            MusicType.GAME_OVER, R.raw.game_over,
            MusicType.BOMB_EXPLOSION, R.raw.bomb_explosion,
            MusicType.HERO_HIT, R.raw.bullet_hit,
            MusicType.HERO_SHOOT, R.raw.bullet,
            MusicType.PROPS, R.raw.get_supply
    );
    private final Map<MusicType, Integer> musicIDMap = new HashMap<>();
    private final Map<MusicType, Boolean> musicNoStop = new HashMap<>();
    private SoundPool soundPool = null;

    protected abstract Context getContext();

    @Override
    public void musicLoadAll() {
        if (soundPool != null) {
            soundPool.release();
        }
        musicIDMap.clear();
        SoundPool.Builder spb = new SoundPool.Builder();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        spb.setMaxStreams(Constants.ANDROID_SOUND_STREAM_MAX);
        // 转换音频格式
        spb.setAudioAttributes(audioAttributes);
        // 创建SoundPool对象
        soundPool = spb.build();
        musicResource.forEach((type, resourceID) -> musicIDMap.put(type, soundPool.load(getContext(), resourceID, 1)));
    }

    public void startMusic(MusicType type, Boolean noStop, Boolean loop) {
        if (!RunningConfig.musicEnable) {
            return;
        }
        musicNoStop.replace(type, noStop);
        try {
            // noinspection ConstantConditions
            soundPool.play(musicIDMap.get(type), 1, 1, 0, loop ? -1 : 0, 1);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startMusic(MusicType type, Boolean noStop) {
        startMusic(type, noStop, false);
    }

    @Override
    public void stopMusic(MusicType type) {
        try {
            // noinspection ConstantConditions
            soundPool.stop(musicIDMap.get(type));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopAllMusic() {
        musicIDMap.forEach((type, id) -> {
            if (!musicNoStop.containsKey(type)) {
                stopMusic(type);
            }
        });
    }

    @Override
    public void startLoopMusic(MusicType type) {
        startMusic(type, false, true);
    }

    static private final AbstractLogger logger = new LoggerAndroid();

    @Override
    public AbstractLogger getLogger() {
        return logger;
    }

    private final Map<String, Integer> resourceMap = Map.of(
            "genshin", R.font.genshin
    );

    @SuppressWarnings("ConstantConditions")
    @Override
    public XFont<?> getFont(String name) {
        if (cachedFont.containsKey(name)) return cachedFont.get(name);
        Typeface font = ResourcesCompat.getFont(getContext(), resourceMap.getOrDefault(name, R.font.genshin));
        XFont<?> f = new XFont<>(font);
        cachedFont.put(name, f);
        return f;
    }
}
