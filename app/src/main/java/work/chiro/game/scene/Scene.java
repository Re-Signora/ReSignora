package work.chiro.game.scene;

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
        System.out.println("Scene start: " + name);
        sceneRunnable.run();
    }
}
