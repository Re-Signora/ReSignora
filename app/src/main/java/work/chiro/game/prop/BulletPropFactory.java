package work.chiro.game.prop;

import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BulletPropFactory extends AbstractPropFactory {
    public BulletPropFactory(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public BulletProp create() {
        return new BulletProp(getPosition(), getAnimateContainer());
    }
}
