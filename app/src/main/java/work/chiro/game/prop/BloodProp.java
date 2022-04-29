package work.chiro.game.prop;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.vector.Vec2;

import java.util.List;

/**
 * @author Chiro
 */
public class BloodProp extends AbstractProp {
    int increaseHp;
    public BloodProp(Vec2 posInit, AnimateContainer animateContainer, int increaseHp) {
        super(posInit, animateContainer);
        this.increaseHp = increaseHp;
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        playSupplyMusic();
        HeroAircraftFactory.getInstance().decreaseHp(-increaseHp);
    }
}
