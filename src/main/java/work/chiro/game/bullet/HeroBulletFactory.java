package work.chiro.game.bullet;

import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class HeroBulletFactory extends BaseBulletFactory {
    final private int index;

    public HeroBulletFactory(Vec2 posInit, int index) {
        super(posInit);
        this.index = index;
    }

    @Override
    public BaseBullet create() {
        return new HeroBullet(
                getPosition(),
                new AnimateContainerFactory(
                        AnimateContainerFactory.ContainerType.ConstSpeed,
                        getPosition())
                        .setupSpeed(new Vec2((index * 2 - HeroAircraftFactory.getInstance().getShootNum() + 1) * 0.06, -2))
                        .create(),
                30);
    }
}
