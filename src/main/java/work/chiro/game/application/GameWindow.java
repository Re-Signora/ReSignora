package work.chiro.game.application;

import work.chiro.game.scene.SceneClient;

import javax.swing.*;

/**
 * @author Chiro
 */
public class GameWindow implements SceneClient {
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
                game = new Game();
            }
        }
        if (game.getStartedFlag() && game.getGameOverFlag()) {
            game.resetStates();
            game.action();
        }
        if (!game.getStartedFlag()) {
            game.action();
        }
        return game;
    }

    @Override
    public Object getWaitObject() {
        return getGame().getWaitObject();
    }

    @Override
    public void nextScene() {
        // 在 Game 中自己调用了 obj.notify() 了
    }

    public void clearGameInstance() {
        game = null;
    }
}
