package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.HeroAircraftFactory;

import java.util.LinkedList;
import java.util.List;

public class BulletProp extends AbstractProp {
    public BulletProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        System.out.println("FireSupply active!");
        HeroAircraftFactory.getInstance().increaseShootNum();
    }
}
