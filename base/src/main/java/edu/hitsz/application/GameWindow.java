package edu.hitsz.application;

import edu.hitsz.scene.SceneClient;

import javax.swing.*;

/**
 * @author Chiro
 */
public class GameWindow implements SceneClient {
    private Game game = null;
    @Override
    public JPanel getPanel() {
        return getGame();
    }

    public Game getGame() {
        if (game == null) {
            synchronized (Game.class) {
                game = new Game();
            }
            game.action();
        }
        return game;
    }

    @Override
    public Object getWaitObject() {
        return getGame().getWaitObject();
    }
}
