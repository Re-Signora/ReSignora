package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraftFactory;
import edu.hitsz.vector.Vec2;

import java.util.List;

/**
 * @author Chiro
 */
public class BulletProp extends AbstractProp {
    public BulletProp(Vec2 posInit) {
        super(posInit);
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        System.out.println("FireSupply active!");
        HeroAircraftFactory.getInstance().increaseShootNum();
    }
}
