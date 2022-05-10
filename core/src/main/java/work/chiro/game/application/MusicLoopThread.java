package work.chiro.game.application;

import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 循环音乐
 *
 * @author Chiro
 */
public class MusicLoopThread extends MusicThread {
    public MusicLoopThread(MusicManager.MusicType type) {
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


