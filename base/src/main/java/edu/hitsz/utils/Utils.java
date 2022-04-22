package edu.hitsz.utils;

import edu.hitsz.application.Game;
import edu.hitsz.application.MusicManager;
import edu.hitsz.application.MusicThread;
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
    public static LinkedList<BaseBullet> letEnemyShoot(Vec2 position) {
        LinkedList<BaseBullet> ret = new LinkedList<>();
        ret.add(new EnemyBulletFactory(position, EnemyBulletFactory.BulletType.Direct).create());
        return ret;
    }

    public static Boolean isInRange(double value, double down, double up) {
        return down <= value && value < up;
    }

    static long timeStartGlobal = 0;

    public static double getTimeMills() {
        if (timeStartGlobal == 0) {
            timeStartGlobal = System.currentTimeMillis();
        }
        return (double) (System.currentTimeMillis() - timeStartGlobal);
    }

    public static void startMusic(MusicManager.MusicType type) {
        Game.getThreadFactory().newThread(new MusicThread(MusicManager.get(type))).start();
    }
}
