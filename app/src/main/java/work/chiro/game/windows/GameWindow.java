package work.chiro.game.windows;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.scene.AbstractSceneClient;

import javax.swing.*;

/**
 * @author Chiro
 */
public class GameWindow extends AbstractSceneClient {
    static GameWindow gameWindow = null;

    public static GameWindow getInstance() {
        if (gameWindow == null) {
            synchronized (GameWindow.class) {
                gameWindow = new GameWindow();
            }
        }
        return gameWindow;
    }

    static private Game game = null;

    @Override
    public JPanel getPanel() {
        return getGame();
    }

    public Game getGame() {
        if (game == null) {
            synchronized (Game.class) {
                game = new Game(RunningConfig.difficulty);
            }
        }
        return game;
    }

    @Override
    public Object getWaitObject() {
        return getGame().getWaitObject();
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
