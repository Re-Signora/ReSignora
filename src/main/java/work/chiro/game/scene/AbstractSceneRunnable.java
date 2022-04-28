package work.chiro.game.scene;

/**
 * @author Chiro
 */
public abstract class AbstractSceneRunnable implements Runnable {
    /**
     * 创建 Client
     *
     * @return client
     */
    public abstract AbstractSceneClient getClient();

    @Override
    public void run() {
        try {
            Object waitObject = getClient().getWaitObject();
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (waitObject) {
                waitObject.wait();
            }
        } catch (InterruptedException e) {
            System.out.println("window exception: " + e);
        }
        synchronized (this) {
            this.notify();
        }
    }
}
