package edu.hitsz.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Chiro
 */
public class HistoryImpl implements HistoryDAO {
    final static String FILENAME = "save.ser";
    private List<HistoryObject> data;

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
    public Boolean updateByUtc(int utc, HistoryObject newHistory) {
        List<HistoryObject> dataNew = data.stream().filter(item -> item.getTime() == utc).collect(Collectors.toList());
        if (!dataNew.isEmpty()) {
            boolean r = Collections.replaceAll(data, dataNew.get(0), newHistory.copy(utc));
            dump();
            return r;
        }
        return false;
    }

    @Override
    public Boolean deleteByUtc(int utc) {
        boolean r = data.removeIf(item -> item.getTime() == utc);
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
                e.printStackTrace();
                data = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display() {
        System.out.println("[ ======= HISTORY ======= ]");
        for (HistoryObject historyObject : data) {
            System.out.println(historyObject.toString());
        }
        System.out.println("[ ======= ------- ======= ]");
    }
}
