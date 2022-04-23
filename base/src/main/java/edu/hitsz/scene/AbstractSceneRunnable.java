package edu.hitsz.scene;

/**
 * @author Chiro
 */
public abstract class AbstractSceneRunnable implements Runnable {
    /**
     * 创建 Client
     *
     * @return client
     */
    public abstract SceneClient getClient();

    @Override
    public void run() {
        System.out.println("window in");
        try {
            Object waitObject = getClient().getWaitObject();
            synchronized (waitObject) {
                waitObject.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("window out");
        synchronized (this) {
            this.notify();
        }
    }
}
