package work.chiro.game.x.activity;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import work.chiro.game.game.Game;
import work.chiro.game.x.ui.XLayoutManager;

public class XActivityManager {
    private final LinkedList<XActivity> activities = new LinkedList<>();
    private final XLayoutManager layoutManager;
    private final Game game;

    public XActivityManager(Game game) {
        this.game = game;
        layoutManager = XLayoutManager.getInstance();
    }

    public XLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public Game getGame() {
        return game;
    }

    private SwitchActivityCallback onSwitchActivity = null;

    public void setOnSwitchActivity(SwitchActivityCallback onSwitchActivity) {
        this.onSwitchActivity = onSwitchActivity;
    }

    public <T extends XActivity> boolean startActivityWithBundle(Class<T> activityClazz, XBundle bundle) {
        try {
            XActivity activity = activityClazz.getConstructor(Game.class).newInstance(game);
            activities.add(activity);
            if (onSwitchActivity != null) {
                onSwitchActivity.run(activities.getLast());
            }
            activity.onCreate(bundle);
            activity.onStart();
            return true;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean finishActivity(XActivity activity) {
        activity.onStop();
        boolean res = activities.remove(activity);
        if (res && onSwitchActivity != null) {
            onSwitchActivity.run(activities.getLast());
        }
        return res;
    }
}
