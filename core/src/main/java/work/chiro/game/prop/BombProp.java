package work.chiro.game.prop;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.compatible.ResourceProvider;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BombProp extends AbstractProp {
    public BombProp(AbstractConfig config,  Vec2 posInit, AnimateContainer animateContainer) {
        super(config, posInit, animateContainer);
    }

    @Override
    public AbstractProp update() {
        Utils.getLogger().info("BombSupply active!");
        ResourceProvider.getInstance().startMusic(MusicType.BOMB_EXPLOSION);
        enemyAircrafts.forEach(AbstractAircraft::onPropHandle);
        enemyBullets.forEach(BaseBullet::onPropHandle);
        return this;
    }
}
