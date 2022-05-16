package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.Constants;
import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class EliteEnemyFactory implements AbstractAircraftFactory {
    public EliteEnemyFactory() {
    }

    @Override
    public EliteEnemy create(AbstractConfig config) {
        config.getEnemyMagnification().update(Utils.getTimeMills());
        Vec2 newPos = new Vec2((Math.random() * (Constants.WINDOW_WIDTH - ImageManager.getInstance().ELITE_ENEMY_IMAGE.getWidth())),
                (Math.random() * Constants.WINDOW_HEIGHT * Constants.ELITE_CREATE_VERTICAL_RANGE));
        if (RunningConfig.difficulty == Difficulty.Easy) {
            return new EliteEnemy(
                    config,
                    newPos,
                    new AnimateContainerFactory(
                            AnimateContainerFactory.ContainerType.ConstSpeed, newPos)
                            .setupSpeed(new Vec2(0, 0.08 * config.getEnemyMagnification().getScaleNow().getX()))
                            .create(),
                    60 * config.getEnemyMagnification().getScaleNow().getX()
            );
        } else {
            return new EliteEnemy(
                    config,
                    newPos,
                    new AnimateContainerFactory(
                            AnimateContainerFactory.ContainerType.SmoothTo, newPos)
                            .setupTimeSpan(config.getEliteJumpTime())
                            .setupTarget(newPos.plus(Utils.randomPosition(new Vec2(-config.getEliteJumpRange().getX(), 0), new Vec2(config.getEliteJumpRange().getX(), config.getEliteJumpRange().getY()))))
                            .setupAnimateCallback(animateContainer -> {
                                animateContainer.clearAllAnimates()
                                        .addAnimate(
                                                new AnimateContainerFactory(AnimateContainerFactory.ContainerType.SmoothTo, newPos)
                                                        .setupTimeSpan(config.getEliteJumpTime())
                                                        .setupTarget(newPos.plus(Utils.randomPosition(new Vec2(-config.getEliteJumpRange().getX(), 0), new Vec2(config.getEliteJumpRange().getX(), config.getEliteJumpRange().getY()))))
                                                        .createAnimate()
                                        );
                                return false;
                            })
                            .create(),
                    90 * config.getEnemyMagnification().getScaleNow().getX()
            );
        }
    }
}
