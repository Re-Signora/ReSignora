package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraftFactory;
import edu.hitsz.vector.Vec2;

import java.util.List;

/**
 * @author Chiro
 */
public class BloodProp extends AbstractProp {
    int increaseHp;
    public BloodProp(Vec2 posInit, int increaseHp) {
        super(posInit);
        this.increaseHp = increaseHp;
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        HeroAircraftFactory.getInstance().decreaseHp(-increaseHp);
    }
}
