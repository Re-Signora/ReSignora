package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * @author Chiro
 */
public class EliteEnemyFactory implements AbstractAircraftFactory {
    public EliteEnemyFactory() {
    }

    @Override
    public EliteEnemy create() {
        return new EliteEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                0,
                2,
                60
        );
    }
}
