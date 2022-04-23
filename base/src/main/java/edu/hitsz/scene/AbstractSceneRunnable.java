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
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (waitObject) {
                waitObject.wait();
            }
        } catch (InterruptedException e) {
            System.out.println("window exception: " + e);
        }
        System.out.println("window out");
        synchronized (this) {
            this.notify();
        }
    }
}
