package edu.hitsz.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 历史记录储存类
 *
 * @author Chiro
 */
public class HistoryObject implements Serializable {
    final private String name;
    final private int score;
    final private long time;
    final private String message;

    public HistoryObject(String name, int score, long time, String message) {
        this.name = name;
        this.score = score;
        this.time = time;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public HistoryObject copy(int newUtc) {
        return new HistoryObject(getName(), getScore(), newUtc, getMessage());
    }

    public String getTimeString() {
        return new SimpleDateFormat().format(time);
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", time=" + getTimeString() +
                ", message='" + message + '\'' +
                '}';
    }
}
