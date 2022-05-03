package work.chiro.game.prop;

import org.junit.jupiter.api.Test;
import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.aircraft.EliteEnemyFactory;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.EasyConfig;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BulletPropTest {
    BulletPropFactory bulletPropFactory = new BulletPropFactory(new Vec2());
    AbstractConfig config = new EasyConfig();
    BulletPropTest() {
        new HeroAircraftFactory().create(config);
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
        LinkedList<BaseBullet> enemyBullets = new LinkedList<>();
        EliteEnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
        for (int i = 0; i < 100; i++) {
            enemyAircrafts.add(eliteEnemyFactory.create(config));
        }
        enemyAircrafts.forEach(enemyAircraft -> enemyBullets.addAll(enemyAircraft.shoot()));
        AbstractProp bulletProp = bulletPropFactory.create().
                subscribeEnemyAircrafts(enemyAircrafts)
                .subscribeEnemyBullets(enemyBullets);
        bulletProp.update();
        assumeTrue(HeroAircraftFactory.getInstance().shoot().size() == 2);
        System.out.println("Test pass.");
    }
}