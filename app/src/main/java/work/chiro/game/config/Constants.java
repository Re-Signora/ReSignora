package work.chiro.game.config;

import java.util.List;

/**
 * 常量
 *
 * @author Chiro
 */
public class Constants {
    public static final double MUSIC_SHOOT_MIN_CYCLE_MS = 100;
    public static final double MUSIC_HERO_HURT_MIN_CYCLE_MS = 300;
    public static final double BOSS_DROP_RANGE = 100;
    public static final double ELITE_CREATE_VERTICAL_RANGE = 0.2;
    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;
    public static final boolean DEBUG_NO_DEATH = false;
    public static final int GAME_POOL_SIZE = 100;
    public static final int DRAW_HP_BAR = 3;
    public static final int INVINCIBLE_TIME = 2000;
    public static final List<Double> HERO_ALPHA_DATA = List.of(0.3, 0.6, 0.8);
}
