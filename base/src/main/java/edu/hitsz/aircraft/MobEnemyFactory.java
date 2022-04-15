package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * @author Chiro
 */
public class MobEnemyFactory implements AbstractAircraftFactory {
    public MobEnemyFactory() {
    }

    @Override
    public MobEnemy create() {
        return new MobEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                0,
                10,
                30);
    }
}
