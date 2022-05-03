package work.chiro.game.prop;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.MusicManager;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.windows.GameWindow;

/**
 * @author Chiro
 */
public class BombProp extends AbstractProp implements PropApplier {
    public BombProp(AbstractConfig config,  Vec2 posInit, AnimateContainer animateContainer) {
        super(config, posInit, animateContainer);
    }

    @Override
    public AbstractProp update() {
        System.out.println("BombSupply active!");
        Utils.startMusic(MusicManager.MusicType.BOMB_EXPLOSION);
        enemyAircrafts.forEach(enemy -> {
            GameWindow.getInstance().getGame().increaseScore(enemy.getScore());
            enemy.vanish();
        });
        enemyBullets.forEach(AbstractFlyingObject::vanish);
        return this;
    }
}
