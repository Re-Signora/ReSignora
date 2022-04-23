package edu.hitsz.scene;

import edu.hitsz.application.Game;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Chiro
 */
public class SceneRun {
    final private JFrame parent;
    final private LinkedList<Scene> sceneList = new LinkedList<>();

    public SceneRun(JFrame parent, List<Scene> sceneList) {
        this.parent = parent;
        this.sceneList.addAll(sceneList);
    }

    public static class SceneRunDoneException extends Exception {
    }

    public void run() throws SceneRunDoneException {
        for (Scene scene : sceneList) {
            Thread nowRunning = Game.getThreadFactory().newThread(scene::run);
            synchronized (nowRunning) {
                try {
                    JPanel panel = scene.getSceneRunnable().getClient().getPanel();
                    parent.setContentPane(panel);
                    parent.setVisible(true);
                    nowRunning.setDaemon(false);
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
        System.out.println("Scene Run done");
        parent.setVisible(false);
    }
}
