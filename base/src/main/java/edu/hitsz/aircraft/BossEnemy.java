package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.Utils;

import java.util.LinkedList;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class BossEnemy extends AbstractAircraft {

    int shootDivider = 2;
    int shootCnt = 0;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void vanish() {
        super.vanish();
        BossEnemyFactory.clearInstance();
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        if (shootCnt >= shootDivider) {
            shootCnt = 0;
            return Utils.letEnemyShoot(getLocationX(), getLocationY());
        } else {
            shootCnt += 1;
        }
        return new LinkedList<>();
    }
}
