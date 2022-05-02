package work.chiro.game.aircraft;

import org.junit.jupiter.api.Test;
import work.chiro.game.bullet.BaseBullet;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BossEnemyTest {
    BossEnemyFactory bossEnemyFactory = new BossEnemyFactory(() -> System.out.println("boss vanish"));
    BossEnemy getBossNewInstance() {
        return bossEnemyFactory.create();
    }

    @Test
    void vanish() {
        BossEnemy dut = getBossNewInstance();
        dut.vanish();
        assumeTrue(dut.notValid());
        System.out.println("Test pass.");
    }

    @Test
    void shoot() {
        BossEnemy dut = getBossNewInstance();
        HeroAircraft heroAircraft = new HeroAircraftFactory().create();
        LinkedList<BaseBullet> bullets = dut.shoot();
        assumeTrue(bullets.size() >= 1);
        System.out.println("Test pass.");
    }

    @Test
    void singleton() {
        BossEnemyFactory.clearInstance();
        BossEnemy dut1 = getBossNewInstance();
        BossEnemy dut2 = getBossNewInstance();
        assumeTrue(dut1 == dut2);
        BossEnemyFactory.clearInstance();
        dut2 = getBossNewInstance();
        assumeFalse(dut1 == dut2);
        System.out.println("Test pass.");
    }
}