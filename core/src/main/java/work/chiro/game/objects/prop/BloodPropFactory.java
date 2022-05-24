package work.chiro.game.objects.prop;

import work.chiro.game.config.Constants;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BloodPropFactory extends AbstractPropFactory {
    public BloodPropFactory(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public BloodProp create() {
        return new BloodProp(getPosition(), getAnimateContainer(), Constants.BLOOD_PROP_INCREASE);
    }
}
