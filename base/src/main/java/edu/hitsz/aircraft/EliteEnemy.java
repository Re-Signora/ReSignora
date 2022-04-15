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
        return Utils.letEnemyShoot(getLocationX(), getLocationY());
    }

    @Override
    public void vanish() {
        super.vanish();

    }
}
