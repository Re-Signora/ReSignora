package work.chiro.game.compatible;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import work.chiro.game.history.HistoryImpl;
import work.chiro.game.history.HistoryObject;

public class HistoryImplAndroid extends HistoryImpl {
    private static HistoryImplAndroid history = null;
    private final static String FILENAME = "save.ser";
    private final SharedPreferences sp;

    synchronized public static HistoryImplAndroid getInstance(Context context) {
        if (history == null) {
            synchronized (HistoryImplAndroid.class) {
                history = new HistoryImplAndroid(context);
            }
        }
        history.sort();
        return history;
    }

    private HistoryImplAndroid(Context context) {
        super(false);
        sp = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        load();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void load() {
        String base64Data = sp.getString("data", null);
        // System.out.println("got base64 String: " + base64Data);
        if (base64Data == null) {
            data = new ArrayList<>();
        } else {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(base64Data, Base64.DEFAULT));
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                // noinspection unchecked
                data = (List<HistoryObject>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("WARN: history data err");
                data = new ArrayList<>();
                dump();
            }
        }
        sort();
    }

    @Override
    public synchronized void dump() {
        sort();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // System.out.println("bao size: " + byteArrayOutputStream.size());
        byte[] bytesData = byteArrayOutputStream.toByteArray();
        String base64Data = Base64.encodeToString(bytesData, Base64.DEFAULT);
        // System.out.println("bao size: " + byteArrayOutputStream.size() + "; write Data: " + base64Data);
        sp.edit().putString("data", base64Data).apply();
    }
}
