package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemyFactory;
import edu.hitsz.aircraft.HeroAircraftFactory;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BulletPropTest {
    BulletPropFactory bulletPropFactory = new BulletPropFactory(0 ,0, 0, 10);
    BulletPropTest() {
        new HeroAircraftFactory(0, 0, 0, 0, 100).create();
    }

    @Test
    void crash() {
        BulletProp bulletProp = bulletPropFactory.create();
        assumeTrue(bulletProp.crash(HeroAircraftFactory.getInstance()));
        System.out.println("Test pass.");
    }

    @Test
    void handleAircrafts() {
        LinkedList<AbstractAircraft> enemyAircrafts = new LinkedList<>();
        EliteEnemyFactory eliteEnemyFactory = new EliteEnemyFactory(0, 0, 0, 10, 100);
        for (int i = 0; i < 100; i++) {
            enemyAircrafts.add(eliteEnemyFactory.create());
        }
        BulletProp bulletProp = bulletPropFactory.create();
        bulletProp.handleAircrafts(enemyAircrafts);
        assumeTrue(HeroAircraftFactory.getInstance().shoot().size() == 2);
        System.out.println("Test pass.");
    }
}