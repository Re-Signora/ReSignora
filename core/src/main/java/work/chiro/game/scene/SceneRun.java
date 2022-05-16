package work.chiro.game.scene;

import work.chiro.game.thread.MyThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chiro
 */
public class SceneRun {
    final private JFrame frame;
    final private LinkedList<Scene> sceneList = new LinkedList<>();
    public static SceneRun instance;
    private Scene nextScene = null;
    private final CardLayout cardLayout;
    private final JPanel wrapperPanel;

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

    protected SceneRun(JFrame frame, List<Scene> sceneList) {
        this.frame = frame;
        this.sceneList.addAll(sceneList);
        // 使用 CardLayout 切换 Panel
        cardLayout = new CardLayout(0, 0);
        wrapperPanel = new JPanel(cardLayout);
        sceneList.forEach(scene -> wrapperPanel.add(scene.getSceneRunnable().getClient().getPanel(), scene.getSceneRunnable().getClient().getClass().getName()));
        frame.setContentPane(wrapperPanel);
        frame.setVisible(true);
    }

    public static class SceneRunDoneException extends Exception {
    }

    public void run() throws SceneRunDoneException {
        if (nextScene == null) {
            throw new SceneRunDoneException();
        }
        Thread nowRunning = MyThreadFactory.getInstance().newThread(nextScene::run);
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (nowRunning) {
            try {
                frame.setTitle("Aircraft War - " + nextScene.getName());
                System.out.println("will show: " + nextScene.getSceneRunnable().getClient().getClass().getName());
                cardLayout.show(wrapperPanel, nextScene.getSceneRunnable().getClient().getClass().getName());
                nowRunning.setDaemon(false);
                nowRunning.start();
                nextScene.getSceneRunnable().getClient().startAction();
                nextScene = null;
                nowRunning.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Scene run: will quit!");
                System.exit(0);
            }
        }
        System.out.println("Scene Run done");
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
