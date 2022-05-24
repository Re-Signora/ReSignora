package work.chiro.game.x.activity;

import java.util.HashMap;
import java.util.Map;

public class XBundle {
    private Map<String, Object> data = new HashMap<>();

    public XBundle(Map<String, Object> data) {
        this.data = data;
    }

    public XBundle() {
    }

    public XBundle putInt(String key, Integer value) {
        data.put(key, value);
        return this;
    }

    public int getInt(String key) {
        return (Integer) data.get(key);
    }

    public XBundle putString(String key, String value) {
        data.put(key, value);
        return this;
    }

    public String getString(String key) {
        return (String) data.get(key);
    }

    public XBundle putBoolean(String key, Boolean value) {
        data.put(key, value);
        return this;
    }

    public Boolean getBoolean(String key) {
        return (Boolean) data.get(key);
    }

    public XBundle putDouble(String key, Double value) {
        data.put(key, value);
        return this;
    }

    public Double getDouble(String key) {
        return (Double) data.get(key);
    }
}
