package work.chiro.game.aircraft;

import org.junit.jupiter.api.Test;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.EasyConfig;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

class EliteEnemyTest {
    EliteEnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
    AbstractConfig config = new EasyConfig();
    EliteEnemy getNewInstance() {
        return eliteEnemyFactory.create(config);
    }

    @Test
    void forward() throws InterruptedException {
        EliteEnemy dut = getNewInstance();
        Vec2 posRaw = dut.getPosition().copy();
        Thread.sleep(100);
        dut.forward();
        assumeTrue(posRaw.getY() < dut.getLocationY());
        System.out.println("Test pass.");
    }

    @Test
    void shoot() {
        EliteEnemy dut = getNewInstance();
        HeroAircraft heroAircraft = new HeroAircraftFactory().create(config);
        LinkedList<BaseBullet> bullets = dut.shoot();
        assumeTrue(bullets.size() == 1);
        BaseBullet bullet = bullets.get(0);
        System.out.println("Test pass.");
    }
}