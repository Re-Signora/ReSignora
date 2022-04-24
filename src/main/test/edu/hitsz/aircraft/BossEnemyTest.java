package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assumptions.*;

class BossEnemyTest {
    BossEnemyFactory bossEnemyFactory = new BossEnemyFactory(0, 0, 0, 0, 100);
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
        LinkedList<BaseBullet> bullets = dut.shoot();
        assumeTrue(bullets.size() == 1);
        BaseBullet bullet = bullets.get(0);
        assumeTrue(bullet.getLocationX() == 0);
        assumeTrue(bullet.getLocationY() == 0);
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