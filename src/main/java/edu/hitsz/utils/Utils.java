package edu.hitsz.utils;

import edu.hitsz.application.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBulletFactory;
import edu.hitsz.vector.Vec2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static edu.hitsz.application.Difficulty.*;

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
        startMusic(type, false);
    }

    public static void startMusic(MusicManager.MusicType type, Boolean noStop) {
        if (Game.musicEnable) {
            Game.getMusicFactory().newMusicThread(type, noStop).start();
        }
    }

    public static void stopMusic(MusicManager.MusicType type) {
        Game.getMusicFactory().stopMusic(type);
    }

    public static void startLoopMusic(MusicManager.MusicType type) {
        if (Game.musicEnable) {
            Game.getMusicFactory().newLoopMusicThread(type).start();
        }
    }

    public static double setInRange(double value, double down, double up) {
        return Math.max(down, Math.min(value, up));
    }

    private static final Map<String, BufferedImage> CACHED_IMAGE = new HashMap<>();

    public static BufferedImage getCachedImage(String filePath) throws IOException {
        synchronized (CACHED_IMAGE) {
            if (CACHED_IMAGE.containsKey(filePath)) {
                return CACHED_IMAGE.get(filePath);
            }
            try {
                BufferedImage image = ImageIO.read(new FileInputStream(Objects.requireNonNull(Utils.class.getClassLoader().getResource("images/" + filePath)).getFile()));
                CACHED_IMAGE.put(filePath, image);
                return image;
            } catch (NullPointerException e) {
                System.exit(-1);
                return null;
            }
        }
    }

    static Map<String, Difficulty> difficultyHashMap = Map.of("简单", Easy, "中等", Medium, "困难", Hard);
    static Map<Difficulty, String> difficultyToStringHashMap = Map.of(Easy, "简单", Medium, "中等", Hard, "困难");

    public static String difficultyToString(Difficulty difficulty) {
        if (difficulty == null || !difficultyToStringHashMap.containsKey(difficulty)) {
            return null;
        }
        return difficultyToStringHashMap.get(difficulty);
    }

    public static Difficulty difficultyFromString(String string) {
        if (string == null || !difficultyHashMap.containsKey(string)) {
            return null;
        }
        return difficultyHashMap.get(string);
    }
}
