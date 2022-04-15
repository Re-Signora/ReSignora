package edu.hitsz.aircraft;

import edu.hitsz.application.Game;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.Utils;
import edu.hitsz.prop.BloodPropFactory;
import edu.hitsz.prop.BombPropFactory;
import edu.hitsz.prop.BulletPropFactory;

import java.util.LinkedList;
import java.util.Random;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractAircraft {

    final Random random = new Random();

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 100);
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
        System.out.println("vanish!");
        double select = random.nextDouble();
        double probability = 0.3;
        if (Utils.isInRange(select, 0, probability)) {
            Game.getProps().add(new BloodPropFactory(getLocationX(), getLocationY()).create());
        } else if (Utils.isInRange(select, probability, probability * 2)) {
            Game.getProps().add(new BombPropFactory(getLocationX(), getLocationY()).create());
        } else if (Utils.isInRange(select, probability * 2, probability * 3)) {
            Game.getProps().add(new BulletPropFactory(getLocationX(), getLocationY()).create());
        }
        // else no props.
    }
}
