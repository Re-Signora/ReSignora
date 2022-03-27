package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;

import java.util.LinkedList;
import java.util.List;

public class BloodProp extends AbstractProp {
    int increaseHp = -1;
    public BloodProp(int locationX, int locationY, int speedX, int speedY, int increaseHp) {
        super(locationX, locationY, speedX, speedY);
        this.increaseHp = increaseHp;
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        HeroAircraft.getInstance().decreaseHp(-increaseHp);
    }
}
