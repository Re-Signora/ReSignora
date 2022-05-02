package work.chiro.game.utils;

import work.chiro.game.application.Game;
import work.chiro.game.application.MusicManager;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.bullet.EnemyBulletFactory;
import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.vector.Vec2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

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
        if (RunningConfig.musicEnable) {
            Game.getMusicFactory().newMusicThread(type, noStop).start();
        }
    }

    public static void stopMusic(MusicManager.MusicType type) {
        Game.getMusicFactory().stopMusic(type);
    }

    public static void startLoopMusic(MusicManager.MusicType type) {
        if (RunningConfig.musicEnable) {
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
                BufferedImage image = ImageIO.read(Objects.requireNonNull(Utils.class.getResourceAsStream("/images/" + filePath)));
                CACHED_IMAGE.put(filePath, image);
                return image;
            } catch (NullPointerException e) {
                System.exit(-1);
                return null;
            }
        }
    }

    static Map<String, Difficulty> difficultyHashMap = Map.of("简单", Difficulty.Easy, "中等", Difficulty.Medium, "困难", Difficulty.Hard);
    static Map<Difficulty, String> difficultyToStringHashMap = Map.of(Difficulty.Easy, "简单", Difficulty.Medium, "中等", Difficulty.Hard, "困难");

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

    static Random random = new Random();

    public static Vec2 randomPosition(Vec2 rangeLeft, Vec2 rangeRight) {
        return rangeRight.minus(rangeLeft).times(new Vec2(random.nextDouble(), random.nextDouble())).plus(rangeLeft);
    }
}
