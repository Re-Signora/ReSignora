package work.chiro.game.music;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.sound.sampled.SourceDataLine;

import work.chiro.game.resource.MusicType;

/**
 * 循环音乐
 *
 * @author Chiro
 */
public class MusicLoopThread extends MusicThread {
    public MusicLoopThread(MusicType type) {
        super(type);
    }

    @Override
    protected void writeDataToAudioBlock(InputStream source, SourceDataLine dataLine, int bufferSize) {
        do {
            byte[] buffer = getSamples();
            InputStream buf = new ByteArrayInputStream(buffer);
            try {
                super.writeDataToAudioBlock(buf, dataLine, bufferSize);
            } catch (InterruptedException e) {
                break;
            }
        } while (true);
    }
}


