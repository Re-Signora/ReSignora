package work.chiro.game.scene;

import javax.swing.*;

/**
 * @author Chiro
 */
public abstract class AbstractSceneClient {
    private final Object waitObject = new Object();

    /**
     * 得到 Panel
     *
     * @return panel
     */
    abstract public JPanel getPanel();

    /**
     * 得到一个用于线程同步的 Object
     *
     * @return obj
     */
    public Object getWaitObject() {
        return waitObject;
    }

    /**
     * 主动切换到下一场景
     */
    public void nextScene(Class<? extends AbstractSceneClient> clazz) {
        SceneRun.getInstance().setNextScene(clazz);
        synchronized (waitObject) {
            waitObject.notify();
        }
    }

    /**
     * 启动该 Scene 的启动函数
     */
    public void startAction() {
    }
}
