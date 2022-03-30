package edu.hitsz;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBulletFactory;

import java.util.LinkedList;

/**
 * 工具类
 * @author Chiro
 */
public class Utils {
    public static LinkedList<BaseBullet> letEnemyShoot(int x, int y) {
        LinkedList<BaseBullet> ret = new LinkedList<>();
        ret.add(new EnemyBulletFactory(x, y, 0, 10, 10).create());
        return ret;
    }
}
