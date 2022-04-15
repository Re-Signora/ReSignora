package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;

import java.util.List;

/**
 * @author Chiro
 */
public class BombProp extends AbstractProp {
    public BombProp(int locationX, int locationY) {
        super(locationX, locationY);
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        System.out.println("BombSupply active!");
        for (AbstractAircraft enemy : enemyAircrafts) {
            enemy.vanish();
        }
    }
}
