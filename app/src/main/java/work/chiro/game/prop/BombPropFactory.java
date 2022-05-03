package work.chiro.game.prop;

import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BombPropFactory extends AbstractPropFactory {
    public BombPropFactory(AbstractConfig config, Vec2 posInit) {
        super(config, posInit);
    }

    @Override
    public BombProp create() {
        return new BombProp(config, getPosition(), getAnimateContainer());
    }
}
