package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;

/**
 * 经营敌机
 * 不可射击
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractAircraft {

    int shootDivider = 4;
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
        LinkedList<BaseBullet> ret =  new LinkedList<>();
        if (shootCnt >= shootDivider) {
            ret.add(new EnemyBullet(getLocationX(), getLocationY(), 0, 10, 10));
            shootCnt = 0;
        } else {
            shootCnt += 1;
        }
        return ret;
    }

}
