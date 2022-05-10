package work.chiro.game.config;

/**
 * 运行参数，从 MainWindow 启动 Game 的参数
 * @author Chiro
 */
public class RunningConfig {
    public static Difficulty difficulty = Difficulty.Easy;
    public static Boolean musicEnable = true;
    public static Boolean autoShoot = true;
    public static double score = 0;
    public static void increaseScore(double increase) {
        score += increase;
    }
}
