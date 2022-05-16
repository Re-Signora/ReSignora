package work.chiro.game.thread;

import work.chiro.game.resource.MusicManager;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Chiro
 */
public class MusicThreadFactory extends MyThreadFactory {
    private final List<MusicThread> musicThreads = new LinkedList<>();

    private static MusicThreadFactory instance = null;

    public static MusicThreadFactory getInstance() {
        if (instance == null) {
            synchronized (MusicThreadFactory.class) {
                instance = new MusicThreadFactory("AircraftWar-Music");
            }
        }
        return instance;
    }

    MusicThreadFactory(String name) {
        super(name);
    }

    public Thread newMusicThread(MusicManager.MusicType type) {
        return newMusicThread(type, false);
    }

    public Thread newMusicThread(MusicManager.MusicType type, Boolean noStop) {
        MusicThread musicThread = new MusicThread(type);
        musicThreads.add(musicThread.setOnStop(() -> musicThreads.remove(musicThread)).setWillStop(!noStop));
        return newThread(musicThread);
    }

    public Thread newLoopMusicThread(MusicManager.MusicType type) {
        MusicThread musicThread = new MusicLoopThread(type);
        musicThreads.add(musicThread.setOnStop(() -> musicThreads.remove(musicThread)));
        return newThread(musicThread);
    }

    public List<MusicThread> getMusicThreads() {
        return musicThreads;
    }

    public void interruptAll() {
        getMusicThreads().forEach(MusicThread::interrupt);
    }

    public void stopMusic(MusicManager.MusicType type) {
        getMusicThreads().stream().filter(musicThread -> musicThread.getMusicType() == type).forEach(MusicThread::interrupt);
    }
}