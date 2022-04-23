package edu.hitsz.scene;

/**
 * @author Chiro
 */
public class Scene {
    final private AbstractSceneRunnable sceneRunnable;
    final private String name;
    public Scene(String name, AbstractSceneRunnable sceneRunnable) {
        this.name = name;
        this.sceneRunnable = sceneRunnable;
    }

    public AbstractSceneRunnable getSceneRunnable() {
        return sceneRunnable;
    }

    public void run() {
        System.out.println("Scene start: " + name);
        sceneRunnable.run();
    }
}
