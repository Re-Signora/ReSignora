package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraftFactory;
import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.application.Game;
import edu.hitsz.vector.Vec2;

import java.util.List;

/**
 * @author Chiro
 */
public class BulletProp extends AbstractProp {
    public BulletProp(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    @Override
    public void handleAircrafts(List<AbstractAircraft> enemyAircrafts) {
        playSupplyMusic();
        System.out.println("FireSupply active!");
        HeroAircraftFactory.getInstance().increaseShootNum();
        Game.getThreadFactory().newThread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            } finally {
                HeroAircraftFactory.getInstance().decreaseShootNum();
            }
        }).start();
    }
}