package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;

import java.util.LinkedList;
import java.util.List;

public class BombProp extends AbstractProp {
    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        System.out.println("BombSupply active!");
        for (AbstractAircraft enemy : enemyAircrafts) enemy.vanish();
    }
}
