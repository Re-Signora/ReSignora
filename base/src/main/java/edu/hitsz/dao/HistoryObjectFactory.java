package edu.hitsz.dao;

/**
 * @author Chiro
 */
public class HistoryObjectFactory {
    final private String name;
    final private int score;
    final private long time;
    final private String message;

    public HistoryObjectFactory(String name, int score, String message) {
        this.name = name;
        this.score = score;
        this.message = message;
        this.time = System.currentTimeMillis();
    }

    public HistoryObject create() {
        return new HistoryObject(name, score, time, message);
    }
}
