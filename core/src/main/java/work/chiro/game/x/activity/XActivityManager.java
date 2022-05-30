package work.chiro.game.x.activity;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import work.chiro.game.game.Game;
import work.chiro.game.x.ui.layout.XLayout;
import work.chiro.game.x.ui.layout.XLayoutManager;

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
            activity.onCreate(bundle);
            if (onSwitchActivity != null) {
                onSwitchActivity.run(activities.getLast());
            }
            activity.onStart();
            return true;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T extends XActivity> boolean startActivityWithBundle(Class<T> activityClazz) {
        return startActivityWithBundle(activityClazz, null);
    }

    public boolean finishActivity(XActivity activity) {
        activity.onStop();
        boolean res = activities.remove(activity);
        if (res && onSwitchActivity != null) {
            onSwitchActivity.run(activities.getLast());
        }
        return res;
    }

    public XLayout getTopLayout() {
        if (activities.size() == 0) return new XLayout();
        return activities.getLast().getLayout();
    }

    public XLayout getSecondaryLayout() {
        if (activities.size() < 2) return null;
        return activities.get(activities.size() - 2).getLayout();
    }
}
