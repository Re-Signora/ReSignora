package work.chiro.game.compatible;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import work.chiro.game.history.HistoryImpl;
import work.chiro.game.history.HistoryObject;

public class HistoryImplAndroid extends HistoryImpl {
    private static HistoryImplAndroid history = null;
    private final static String FILENAME = "save.ser";
    private final static int FILE_MODE = MODE_PRIVATE;
    private final SharedPreferences sp;
    private List<HistoryObject> data;

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
        sp = context.getSharedPreferences(FILENAME, FILE_MODE);
        load();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void load() {
        String gotString = sp.getString("data", null);
        if (gotString == null) {
            data = new LinkedList<>();
        } else {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(gotString, Base64.DEFAULT));
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                // noinspection unchecked
                data = (List<HistoryObject>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("WARN: history data err");
                data = new LinkedList<>();
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
        sp.edit().putString("data", Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)).apply();
    }
}
