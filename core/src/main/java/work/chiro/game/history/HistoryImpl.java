package work.chiro.game.history;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import work.chiro.game.config.Difficulty;
import work.chiro.game.utils.Utils;

/**
 * @author Chiro
 */
public class HistoryImpl implements HistoryDAO {
    private static HistoryImpl history = null;
    private final static String FILENAME = "save.ser";
    protected List<HistoryObject> data;

    synchronized public static HistoryImpl getInstance() {
        if (history == null) {
            synchronized (HistoryImpl.class) {
                history = new HistoryImpl(true);
            }
        }
        history.sort();
        return history;
    }

    protected HistoryImpl(boolean loadNow) {
        data = new ArrayList<>();
        if (loadNow) {
            load();
        }
    }

    synchronized public List<HistoryObject> getAll() {
        load();
        return data;
    }

    synchronized public Optional<HistoryObject> getByName(String name) {
        return Optional.empty();
    }

    synchronized public Boolean updateByTime(long time, HistoryObject newHistory) {
        List<HistoryObject> dataNew = data.stream().filter(item -> item.getTime() == time).collect(Collectors.toList());
        if (!dataNew.isEmpty()) {
            boolean r = Collections.replaceAll(data, dataNew.get(0), newHistory.copy(time));
            dump();
            return r;
        }
        return false;
    }

    synchronized public Boolean deleteByTime(long time) {
        boolean r = data.removeIf(item -> item.getTime() == time);
        dump();
        return r;
    }

    synchronized public void deleteAll() {
        data.clear();
        dump();
    }

    synchronized public void addOne(HistoryObject historyObject) {
        data.add(historyObject);
        dump();
    }

    synchronized public void dump() {
        sort();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FILENAME);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            File file = new File(FILENAME);
            if (file.exists()) {
                // noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }

    @SuppressWarnings("unchecked")
    synchronized public void load() {
        try {
            File file = new File(FILENAME);
            if (!file.exists()) {
                // noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                data = new ArrayList<>();
                return;
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                // noinspection unchecked
                data = (List<HistoryObject>) objectInputStream.readObject();
            } catch (ClassNotFoundException | EOFException e) {
                System.out.println("Warning: class not found! " + e);
                data = new ArrayList<>();
                dump();
            } catch (InvalidClassException | ClassCastException e) {
                System.out.println("Warning: save file has wrong version with running one! " + e);
                data = new ArrayList<>();
                dump();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sort();
    }

    synchronized public void sort() {
        data.sort(Comparator.comparing(HistoryObject::getScore).reversed());
    }

    synchronized public void display() {
        Utils.getLogger().info("\t\t[ ======= HISTORY ======= ]");
        Utils.getLogger().info("  Name\t   Score\t\t   Time\t\t   Message");
        sort();
        for (HistoryObject historyObject : data) {
            Utils.getLogger().info(historyObject.toString());
        }
        Utils.getLogger().info("\t\t[ ======= ------- ======= ]");
    }

    synchronized public void set(List<HistoryObject> data) {
        this.data = data;
        dump();
    }

    synchronized public List<HistoryObject> getByDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            return getAll();
        }
        return getAll().stream().filter(historyObject -> historyObject.getDifficulty() == difficulty).collect(Collectors.toList());
    }
}
