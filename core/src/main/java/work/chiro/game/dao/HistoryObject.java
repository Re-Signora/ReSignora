package work.chiro.game.dao;

import work.chiro.game.config.Difficulty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 历史记录储存类
 *
 * @author Chiro
 */
public class HistoryObject implements Serializable {
    final private String name;
    final private double score;
    final private long time;
    final private String message;
    final private Difficulty difficulty;

    public HistoryObject(String name, double score, long time, String message, Difficulty difficulty) {
        this.name = name;
        this.score = score;
        this.time = time;
        this.message = message;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public HistoryObject copy(long newTime) {
        return new HistoryObject(getName(), getScore(), newTime, getMessage(), difficulty);
    }

    public String getTimeString() {
        return new SimpleDateFormat().format(time);
    }

    @Override
    public String toString() {
        return "" + difficulty + "\t" + name + "\t" + String.format("%9d", (int) (score)) + "\t" + getTimeString() + "\t" + message;
    }

    static public List<String> getLabels() {
        return Arrays.asList("难度", "名称", "分数", "时间", "留言");
    }

    public List<Object> getDataAsList() {
        return Arrays.asList(difficulty, name, score, getTimeString(), message);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
