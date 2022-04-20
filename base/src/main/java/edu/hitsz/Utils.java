package edu.hitsz;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBulletFactory;
import edu.hitsz.vector.Vec2;

import java.util.LinkedList;

/**
 * 工具类
 *
 * @author Chiro
 */
public class Utils {
    public static LinkedList<BaseBullet> letEnemyShoot(double x, double y) {
        LinkedList<BaseBullet> ret = new LinkedList<>();
        ret.add(new EnemyBulletFactory(x, y, 0, 6, 10).create());
        return ret;
    }

    public static LinkedList<BaseBullet> letEnemyShoot(Vec2 position) {
        return letEnemyShoot(position.getX(), position.getY());
    }

    public static Boolean isInRange(double value, double down, double up) {
        return down <= value && value < up;
    }

    static long timeStartGlobal = System.currentTimeMillis();

    public static double getTimeMills() {
        return (double) (System.currentTimeMillis() - timeStartGlobal);
    }
}
