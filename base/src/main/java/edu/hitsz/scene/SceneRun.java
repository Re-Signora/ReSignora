package edu.hitsz.scene;

import edu.hitsz.application.Game;

import javax.swing.*;
import java.util.List;

/**
 * @author Chiro
 */
public class SceneRun {
    final private JFrame parent;
    final private List<Scene> sceneList;

    public SceneRun(JFrame parent, List<Scene> sceneList) {
        this.parent = parent;
        this.sceneList = sceneList;
    }

    public void run() {
        Thread nowRunning;
        for (Scene scene : sceneList) {
            nowRunning = Game.getThreadFactory().newThread(scene::run);
            synchronized (nowRunning) {
                try {
                    JPanel panel = scene.getSceneRunnable().getClient().getPanel();
                    parent.setContentPane(panel);
                    parent.setVisible(true);
                    nowRunning.start();
                    System.out.println(Thread.currentThread() + "waiting");
                    nowRunning.wait();
                    System.out.println("continue");
                    parent.remove(panel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
