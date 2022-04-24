package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

class EliteEnemyTest {
    EliteEnemyFactory eliteEnemyFactory = new EliteEnemyFactory(0, 0, 0, 10, 100);
    EliteEnemy getNewInstance() {
        return eliteEnemyFactory.create();
    }

    @Test
    void forward() {
        EliteEnemy dut = getNewInstance();
        dut.forward();
        assumeTrue(dut.getLocationY() == 10);
        System.out.println("Test pass.");
    }

    @Test
    void shoot() {
        EliteEnemy dut = getNewInstance();
        LinkedList<BaseBullet> bullets = dut.shoot();
        assumeTrue(bullets.size() == 1);
        BaseBullet bullet = bullets.get(0);
        bullet.forward();
        assumeTrue(bullet.getLocationX() == 0);
        assumeTrue(bullet.getLocationY() == 10);
        System.out.println("Test pass.");
    }
}