package work.chiro.game.objects.prop;

import work.chiro.game.objects.aircraft.HeroAircraftFactory;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BloodProp extends AbstractProp {
    int increaseHp;
    public BloodProp( Vec2 posInit, AnimateContainer animateContainer, int increaseHp) {
        super(posInit, animateContainer);
        this.increaseHp = increaseHp;
    }

    @Override
    public AbstractProp update() {
        playSupplyMusic();
        HeroAircraftFactory.getInstance().decreaseHp(-increaseHp);
        return this;
    }
}
