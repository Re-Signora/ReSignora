package edu.hitsz.application;

import javax.sound.sampled.*;
import java.io.*;

/**
 * 循环音乐
 *
 * @author Chiro
 */
public class MusicLoopThread extends MusicThread {
    public MusicLoopThread(String filename) {
        super(filename);
    }

    @Override
    protected void writeDataToAudioBlock(InputStream source, SourceDataLine dataLine, int bufferSize) {
        do {
            byte[] buffer = getSamples().clone();
            InputStream buf = new ByteArrayInputStream(buffer);
            try {
                super.writeDataToAudioBlock(buf, dataLine, bufferSize);
            } catch (InterruptedException e) {
                break;
            }
        } while (true);
    }
}


