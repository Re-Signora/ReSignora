package work.chiro.game.config;

/**
 * 运行参数，从 MainWindow 启动 Game 的参数
 *
 * @author Chiro
 */
public class RunningConfig {
    /**
     * 是否处于调试模式，设置后 Logger 会输出 debug 内容。<br/>
     * 有很多。
     */
    // public static boolean debug = true;
    public static boolean debug = false;

    /**
     * 当前难度
     */
    public static Difficulty difficulty = Difficulty.Easy;
    /**
     * 是否开启音效
     */
    // public static Boolean musicEnable = true;
    public static Boolean musicEnable = false;
    /**
     * 是否开启自动射击
     */
    public static Boolean autoShoot = true;
    /**
     * 当前分数
     */
    public static double score = 0;

    /**
     * 当前动态 Config
     */
    public static AbstractConfig config = new EasyConfig();

    /**
     * 绘图场地长度，实际显示会先按比例缩放到屏幕指定尺寸
     */
    public static int windowWidth = 2400;
    /**
     * 绘图场地高度，实际显示会先按比例缩放到屏幕指定尺寸
     */
    public static int windowHeight = 1080;

    /**
     * 是否窗口缩放，设置为 true 后：<br/>
     * PC: 窗口可拉伸大小，并且动态改变上面的 `windowWidth` 和 `windowHeight` <br/>
     * Android: 窗口可变大小，并且根据实际屏幕大小动态改变上面的 `windowWidth` 和 `windowHeight`
     */
    public static boolean allowResize = false;
    // public static boolean allowResize = true;

    // set this when running on android
    /**
     * 设置是否缩放背景，设置为 `true` 则依据上面的 `windowWidth` 和 `windowHeight` 改变背景图片大小，<br/>
     * 这对于 Java2D 来说性能消耗太大，所以只在 Android 端开启，PC 端请初始化时候手动关闭
     */
    public static boolean scaleBackground = true;

    /**
     * 加分
     *
     * @param increase 加多少分
     */
    public static void increaseScore(double increase) {
        score += increase;
    }

    /**
     * 判断当前运行环境是 PC 还是 Android，用于判断 XView 是否显示等
     */
    public static boolean modePC = true;
    /**
     * 是否启用图片缓存，对 PC 来说是必要的，关闭后性能很渣
     */
    public static boolean enableImageCache = true;

    /**
     * 默认血条高度
     */
    public static int drawHpBar = 5;

    /**
     * 是否启用硬件加速。<br/>
     * PC: OpenGL 加速<br/>
     * Android: Hardware Canvas 加速
     */
    public static boolean enableHardwareSpeedup = true;
//     public static boolean enableHardwareSpeedup = false;

    /**
     * 默认联机服务器，在启动 Game 前修改
     */
    // public static String targetServerHost = "127.0.0.1";
    public static String targetServerHost = "pc.chiro.work";
    /**
     * 默认联机端口，设置在 2048 以上就不需要特殊权限了（好臭）
     */
    public static int targetServerPort = 11451;

    /**
     * 设置发送信息计数器，间隔 `eventSendDivide` 帧才会发送一次数据（最小为 1）
     */
    public static int eventSendDivide = 1;

    public static boolean lowFpsWarn = true;
}
