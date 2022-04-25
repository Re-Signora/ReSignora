package edu.hitsz.application;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Chiro
 */
public class MusicThreadFactory extends MyThreadFactory {
    private final List<MusicThread> musicThreads = new LinkedList<>();
    public MusicThreadFactory(String name) {
        super(name);
    }

    public Thread newMusicThread(MusicManager.MusicType type) {
        MusicThread musicThread = new MusicThread(MusicManager.get(type));
        musicThreads.add(musicThread.setOnStop(() -> musicThreads.remove(musicThread)));
        return newThread(musicThread);
    }

    public Thread newLoopMusicThread(MusicManager.MusicType type) {
        MusicThread musicThread = new MusicLoopThread(MusicManager.get(type));
        musicThreads.add(musicThread.setOnStop(() -> musicThreads.remove(musicThread)));
        return newThread(musicThread);
    }

    public List<MusicThread> getMusicThreads() {
        return musicThreads;
    }
}
