package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.Utils;

import java.util.LinkedList;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractAircraft {

    int shootDivider = 2;
    int shootCnt = 0;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
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
