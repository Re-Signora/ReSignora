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
    protected void writeDataToAudio(InputStream source, SourceDataLine dataLine, int bufferSize) {
        while (true) {
            try {
                byte[] buffer = getSamples().clone();
                InputStream buf = new ByteArrayInputStream(buffer);
                super.writeDataToAudio(buf, dataLine, bufferSize);
                //noinspection BusyWait
                Thread.sleep(getTime() / 1000);
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Music thread quiting");
                    throw new InterruptedException();
                } else {
                    System.out.println("Music loop running");
                }
            } catch (InterruptedException e) {
                System.out.println("Music loop interrupted");
                break;
            }
        }
    }
}


