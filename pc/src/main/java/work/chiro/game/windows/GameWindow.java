package work.chiro.game.windows;

import javax.swing.JPanel;

import work.chiro.game.application.GamePanel;
import work.chiro.game.scene.AbstractSceneClient;

/**
 * @author Chiro
 */
public class GameWindow extends AbstractSceneClient {
    private static GameWindow gameWindow = null;

    public static GameWindow getInstance() {
        if (gameWindow == null) {
            synchronized (GameWindow.class) {
                gameWindow = new GameWindow();
            }
        }
        return gameWindow;
    }

    static private GamePanel game = null;

    @Override
    public JPanel getPanel() {
        return getGamePanel();
    }

    public GamePanel getGamePanel() {
        if (game == null) {
            synchronized (GamePanel.class) {
                game = new GamePanel();
            }
        }
        return game;
    }

    @Override
    public Object getWaitObject() {
        return getGamePanel().getWaitObject();
    }

    @Override
    public void nextScene(Class<? extends AbstractSceneClient> clazz) {
        // Game 中自己调用
    }

    public void clearGameInstance() {
        game = null;
    }

    @Override
    public void startAction() {
        game.resetStates();
        game.action();
    }
}
