package work.chiro.game.prop;

import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BombPropFactory extends AbstractPropFactory {
    public BombPropFactory(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public BombProp create() {
        return new BombProp(getPosition(), getAnimateContainer());
    }
}
