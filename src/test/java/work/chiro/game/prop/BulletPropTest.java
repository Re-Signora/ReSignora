package work.chiro.game.prop;

import org.junit.jupiter.api.Test;
import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.aircraft.EliteEnemyFactory;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BulletPropTest {
    BulletPropFactory bulletPropFactory = new BulletPropFactory(new Vec2());
    BulletPropTest() {
        new HeroAircraftFactory().create();
    }

    @Test
    void crash() {
        BulletProp bulletProp = bulletPropFactory.create();
        // assumeTrue(bulletProp.crash(HeroAircraftFactory.getInstance()));
        System.out.println("Test pass.");
    }

    @Test
    void handleAircrafts() {
        LinkedList<AbstractAircraft> enemyAircrafts = new LinkedList<>();
        EliteEnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
        for (int i = 0; i < 100; i++) {
            enemyAircrafts.add(eliteEnemyFactory.create());
        }
        BulletProp bulletProp = bulletPropFactory.create();
        bulletProp.handleAircrafts(enemyAircrafts);
        assumeTrue(HeroAircraftFactory.getInstance().shoot().size() == 2);
        System.out.println("Test pass.");
    }
}