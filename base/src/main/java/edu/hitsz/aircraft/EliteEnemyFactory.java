package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public class EliteEnemyFactory implements AbstractAircraftFactory {
    public EliteEnemyFactory() {
    }

    @Override
    public EliteEnemy create() {
        Vec2 newPos = new Vec2((Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (Math.random() * Main.WINDOW_HEIGHT * 0.2));
        return new EliteEnemy(
                newPos,
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.ConstSpeed, newPos)
                        .setup(new Vec2(0, 2))
                        .create(),
                60
        );
    }
}
