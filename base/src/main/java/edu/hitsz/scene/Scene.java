package edu.hitsz.scene;

import javax.swing.*;

/**
 * @author Chiro
 */
public class Scene {
    final private SceneRunnable sceneRunnable;
    final private String name;
    public Scene(String name, SceneRunnable sceneRunnable) {
        this.name = name;
        this.sceneRunnable = sceneRunnable;
    }

    public SceneRunnable getSceneRunnable() {
        return sceneRunnable;
    }

    public void run() {
        System.out.println("Scene run: " + name + " " + Thread.currentThread());
        sceneRunnable.run();
        System.out.println("Scene " + name + " finished");
    }
}
