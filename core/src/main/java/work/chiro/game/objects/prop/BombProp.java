package work.chiro.game.objects.prop;

import work.chiro.game.objects.aircraft.AbstractAircraft;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.bullet.BaseBullet;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BombProp extends AbstractProp {
    public BombProp( Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
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
