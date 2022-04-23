package edu.hitsz.dao;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Chiro
 */
public class HistoryImpl implements HistoryDAO {
    private static HistoryImpl history = null;
    private final static String FILENAME = "save.ser";
    private List<HistoryObject> data;

    public static HistoryImpl getInstance() {
        if (history == null) {
            synchronized (HistoryImpl.class) {
                history = new HistoryImpl();
            }
        }
        history.sort();
        return history;
    }

    public HistoryImpl() {
        data = new ArrayList<>();
        load();
    }

    @Override
    public List<HistoryObject> getAll() {
        load();
        return data;
    }

    @Override
    public Optional<HistoryObject> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public Boolean updateByTime(long time, HistoryObject newHistory) {
        List<HistoryObject> dataNew = data.stream().filter(item -> item.getTime() == time).collect(Collectors.toList());
        if (!dataNew.isEmpty()) {
            boolean r = Collections.replaceAll(data, dataNew.get(0), newHistory.copy(time));
            dump();
            return r;
        }
        return false;
    }

    @Override
    public Boolean deleteByTime(long time) {
        boolean r = data.removeIf(item -> item.getTime() == time);
        dump();
        return r;
    }

    @Override
    public void deleteAll() {
        data.clear();
        dump();
    }

    @Override
    public void addOne(HistoryObject historyObject) {
        data.add(historyObject);
        dump();
    }

    @Override
    public void dump() {
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

    @Override
    public void load() {
        try {
            File file = new File(FILENAME);
            if (!file.exists()) {
                // noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                data = new ArrayList<>();
                return;
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            try {
                // noinspection unchecked
                data = (List<HistoryObject>) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Warning: class not found! " + e);
                data = new ArrayList<>();
            } catch (InvalidClassException e) {
                System.out.println("Warning: save file has wrong version with running one! " + e);
                data = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sort();
    }

    public void sort() {
        data.sort(Comparator.comparing(HistoryObject::getScore).reversed());
    }

    public void display() {
        System.out.println("\t\t[ ======= HISTORY ======= ]");
        System.out.println("  Name\t   Score\t\t   Time\t\t   Message");
        sort();
        for (HistoryObject historyObject : data) {
            System.out.println(historyObject.toString());
        }
        System.out.println("\t\t[ ======= ------- ======= ]");
    }
}
