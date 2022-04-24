package edu.hitsz.scene;

import javax.swing.*;

/**
 * @author Chiro
 */
public interface SceneClient {
    /**
     * 得到 Panel
     * @return panel
     */
    JPanel getPanel();

    /**
     * 得到一个用于线程同步的 Object
     * @return obj
     */
    Object getWaitObject();

    /**
     * 主动切换到下一场景
     */
    void nextScene();
}
