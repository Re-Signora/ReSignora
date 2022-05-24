package work.chiro.game.objects.aircraft;

import work.chiro.game.animate.AnimateContainerFactory;
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
    public EliteEnemy create() {
        RunningConfig.config.getEnemyMagnification().update(Utils.getTimeMills());
        Vec2 newPos = new Vec2((Math.random() * (RunningConfig.windowWidth - ImageManager.getInstance().ELITE_ENEMY_IMAGE.getWidth())),
                (Math.random() * RunningConfig.windowHeight * Constants.ELITE_CREATE_VERTICAL_RANGE));
        if (RunningConfig.difficulty == Difficulty.Easy) {
            return new EliteEnemy(
                    newPos,
                    new AnimateContainerFactory(
                            AnimateContainerFactory.ContainerType.ConstSpeed, newPos)
                            .setupSpeed(new Vec2(0, 0.08 * RunningConfig.config.getEnemyMagnification().getScaleNow().getX()))
                            .create(),
                    60 * RunningConfig.config.getEnemyMagnification().getScaleNow().getX()
            );
        } else {
            return new EliteEnemy(
                    newPos,
                    new AnimateContainerFactory(
                            AnimateContainerFactory.ContainerType.SmoothTo, newPos)
                            .setupTimeSpan(RunningConfig.config.getEliteJumpTime())
                            .setupTarget(newPos.plus(Utils.randomPosition(new Vec2(-RunningConfig.config.getEliteJumpRange().getX(), 0), new Vec2(RunningConfig.config.getEliteJumpRange().getX(), RunningConfig.config.getEliteJumpRange().getY()))))
                            .setupAnimateCallback(animateContainer -> {
                                animateContainer.clearAllAnimates()
                                        .addAnimate(
                                                new AnimateContainerFactory(AnimateContainerFactory.ContainerType.SmoothTo, newPos)
                                                        .setupTimeSpan(RunningConfig.config.getEliteJumpTime())
                                                        .setupTarget(newPos.plus(Utils.randomPosition(new Vec2(-RunningConfig.config.getEliteJumpRange().getX(), 0), new Vec2(RunningConfig.config.getEliteJumpRange().getX(), RunningConfig.config.getEliteJumpRange().getY()))))
                                                        .createAnimate()
                                        );
                                return false;
                            })
                            .create(),
                    90 * RunningConfig.config.getEnemyMagnification().getScaleNow().getX()
            );
        }
    }
}
