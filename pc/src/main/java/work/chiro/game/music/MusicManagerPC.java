package work.chiro.game.music;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.resource.MusicType;

/**
 * @author Chiro
 */
public class MusicManagerPC {
    static class ErrorMusicTypeException extends Throwable {
    }

    static public void initAll() {
        MUSIC_FILENAME_MAP.forEach((type, filename) -> get(type));
    }

    static private byte[] getSamplesFromStream(AudioInputStream stream, AudioFormat audioFormat) {
        int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
        byte[] samples = new byte[size];
        DataInputStream dataInputStream = new DataInputStream(stream);
        try {
            dataInputStream.readFully(samples);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return samples;
    }

    static private byte[] getMusicFileData(MusicType musicType) throws ErrorMusicTypeException {
        String filename = getFilename(musicType);
        try {
            byte[] data = ResourceProvider.getInstance().getSoundBytesFromResource(filename);
            if (!MUSIC_DATA_MAP.containsKey(musicType)) {
                MUSIC_DATA_MAP.put(musicType, data);
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    static private InputStream getMusicFileDataAsStream(MusicType musicType) throws ErrorMusicTypeException {
        return new ByteArrayInputStream(getMusicFileData(musicType));
    }

    static private byte[] readMusicSamples(MusicType musicType) throws ErrorMusicTypeException {
        try {
            // 定义一个 AudioInputStream 用于接收输入的音频数据，使用 AudioSystem 来获取音频的音频输入流
            AudioInputStream stream = AudioSystem.getAudioInputStream(getMusicFileDataAsStream(musicType));
            // 用 AudioFormat 来获取 AudioInputStream 的格式
            AudioFormat audioFormat = stream.getFormat();
            if (!MUSIC_AUDIO_FORMAT_MAP.containsKey(musicType)) {
                MUSIC_AUDIO_FORMAT_MAP.put(musicType, audioFormat);
            }
            return getSamplesFromStream(stream, audioFormat);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    final static private Map<MusicType, byte[]> MUSIC_SAMPLES_MAP = new HashMap<>();
    final static private Map<MusicType, byte[]> MUSIC_DATA_MAP = new HashMap<>();
    final static private Map<MusicType, String> MUSIC_FILENAME_MAP = Map.of(
            MusicType.BGM, "bgm.wav",
            MusicType.BGM_BOSS, "bgm_boss.wav",
            MusicType.BOMB_EXPLOSION, "bomb_explosion.wav",
            MusicType.HERO_SHOOT, "bullet.wav",
            MusicType.HERO_HIT, "bullet_hit.wav",
            MusicType.PROPS, "get_supply.wav",
            MusicType.GAME_OVER, "game_over.wav"
    );
    final static private Map<MusicType, AudioFormat> MUSIC_AUDIO_FORMAT_MAP = new HashMap<>();

    static public byte[] get(MusicType type) {
        if (!MUSIC_SAMPLES_MAP.containsKey(type)) {
            byte[] data = new byte[0];
            try {
                data = readMusicSamples(type);
            } catch (ErrorMusicTypeException e) {
                e.printStackTrace();
            }
            MUSIC_SAMPLES_MAP.put(type, data);
            return data;
        }
        return MUSIC_SAMPLES_MAP.get(type);
    }

    static private String getFilename(MusicType type) throws ErrorMusicTypeException {
        if (!MUSIC_FILENAME_MAP.containsKey(type)) {
            throw new ErrorMusicTypeException();
        }
        return MUSIC_FILENAME_MAP.get(type);
    }

    public static AudioFormat getMusicAudioFormat(MusicType type) {
        return MUSIC_AUDIO_FORMAT_MAP.get(type);
    }
}
