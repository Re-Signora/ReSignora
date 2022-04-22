package edu.hitsz.scene;

/**
 * @author Chiro
 */
public interface SceneRunnable extends Runnable {
    /**
     * 创建 Client
     * @return client
     */
    SceneClient getClient();
}
