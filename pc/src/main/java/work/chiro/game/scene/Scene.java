package work.chiro.game.scene;

import work.chiro.game.utils.Utils;

/**
 * @author Chiro
 */
public class Scene {
    final private AbstractSceneRunnable sceneRunnable;
    final private String name;
    public Scene(String name, AbstractSceneClient client) {
        this.name = name;
        this.sceneRunnable = new AbstractSceneRunnable() {
            @Override
            public AbstractSceneClient getClient() {
                return client;
            }
        };
    }

    public String getName() {
        return name;
    }

    public AbstractSceneRunnable getSceneRunnable() {
        return sceneRunnable;
    }

    public void run() {
        Utils.getLogger().info("Scene start: " + name);
        sceneRunnable.run();
    }
}
