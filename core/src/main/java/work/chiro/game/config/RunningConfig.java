package work.chiro.game.config;

/**
 * 运行参数，从 MainWindow 启动 Game 的参数
 *
 * @author Chiro
 */
public class RunningConfig {
    public static Difficulty difficulty = Difficulty.Easy;
    // public static Boolean musicEnable = true;
    public static Boolean musicEnable = false;
    public static Boolean autoShoot = true;
    public static double score = 0;

    public static int windowWidth = 2400;
    public static int windowHeight = 1080;

    public static boolean allowResize = false;
    // public static boolean allowResize = true;

    public static void increaseScore(double increase) {
        score += increase;
    }
}
