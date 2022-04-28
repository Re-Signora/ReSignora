package work.chiro.game.scene;

import work.chiro.game.application.Game;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chiro
 */
public class SceneRun {
    final private JFrame parent;
    final private LinkedList<Scene> sceneList = new LinkedList<>();
    public static SceneRun instance;
    private Scene nextScene = null;

    public static SceneRun newInstance(JFrame parent, List<Scene> sceneList) {
        if (instance == null) {
            synchronized (SceneRun.class) {
                SceneRun.instance = new SceneRun(parent, sceneList);
            }
        }
        return instance;
    }

    public static SceneRun getInstance() {
        return instance;
    }

    protected SceneRun(JFrame parent, List<Scene> sceneList) {
        this.parent = parent;
        this.sceneList.addAll(sceneList);
    }

    public static class SceneRunDoneException extends Exception {
    }

    public void run() throws SceneRunDoneException {
        if (nextScene == null) {
            throw new SceneRunDoneException();
        }
        Thread nowRunning = Game.getThreadFactory().newThread(nextScene::run);
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (nowRunning) {
            try {
                JPanel panel = nextScene.getSceneRunnable().getClient().getPanel();
                nextScene = null;
                parent.setContentPane(panel);
                parent.setVisible(true);
                nowRunning.setDaemon(false);
                nowRunning.start();
                nowRunning.wait();
                parent.remove(panel);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Scene run: will quit!");
                System.exit(0);
            }
        }
        System.out.println("Scene Run done");
        parent.setVisible(false);
        if (nextScene != null) {
            run();
        }
    }

    public SceneRun setNextScene(Scene nextScene) {
        this.nextScene = nextScene;
        return this;
    }

    public SceneRun setNextScene(Class<? extends AbstractSceneClient> clazz) {
        List<Scene> res = sceneList.stream().filter(scene -> scene.getSceneRunnable().getClient().getClass() == clazz).collect(Collectors.toList());
        if (res.size() == 0) {
            throw new IllegalArgumentException("Unavailable class!");
        }
        return setNextScene(res.get(0));
    }
}
