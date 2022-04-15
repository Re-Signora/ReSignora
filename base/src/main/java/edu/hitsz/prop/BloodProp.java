package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraftFactory;

import java.util.List;

/**
 * @author Chiro
 */
public class BloodProp extends AbstractProp {
    int increaseHp;
    public BloodProp(int locationX, int locationY, int increaseHp) {
        super(locationX, locationY);
        this.increaseHp = increaseHp;
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        HeroAircraftFactory.getInstance().decreaseHp(-increaseHp);
    }
}
