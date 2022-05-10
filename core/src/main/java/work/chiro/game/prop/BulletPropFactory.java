package work.chiro.game.prop;

import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BulletPropFactory extends AbstractPropFactory {
    public BulletPropFactory(AbstractConfig config, Vec2 posInit) {
        super(config, posInit);
    }

    @Override
    public BulletProp create() {
        return new BulletProp(config, getPosition(), getAnimateContainer());
    }
}
