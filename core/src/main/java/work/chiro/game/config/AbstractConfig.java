package work.chiro.game.config;

import work.chiro.game.timer.TimerLinearChange;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * 配置参数类
 *
 * @author Chiro
 */
public abstract class AbstractConfig {
    protected TimerLinearChange mobCreate = new TimerLinearChange(700);
    protected TimerLinearChange eliteCreate = new TimerLinearChange(1200);
    protected TimerLinearChange enemyShoot = new TimerLinearChange(200);
    protected TimerLinearChange bossShoot = new TimerLinearChange(200);
    protected TimerLinearChange heroShoot = new TimerLinearChange(30d, 1e-3, 10d);
    protected TimerLinearChange bossScoreThreshold = new TimerLinearChange(1000);
    protected TimerLinearChange dropPropsRate = new TimerLinearChange(0.3);
    protected TimerLinearChange enemyMagnification = new TimerLinearChange(1d);
    protected TimerLinearChange bossInitialHp = new TimerLinearChange(3000);

    public TimerLinearChange getMobCreate() {
        return mobCreate;
    }

    public TimerLinearChange getEliteCreate() {
        return eliteCreate;
    }

    public TimerLinearChange getEnemyShoot() {
        return enemyShoot;
    }

    public TimerLinearChange getHeroShoot() {
        return heroShoot;
    }

    public TimerLinearChange getBossShoot() {
        return bossShoot;
    }

    public TimerLinearChange getBossScoreThreshold() {
        return bossScoreThreshold;
    }

    public TimerLinearChange getDropPropsRate() {
        return dropPropsRate;
    }

    public TimerLinearChange getEnemyMagnification() {
        return enemyMagnification;
    }

    public TimerLinearChange getBossInitialHp() {
        return bossInitialHp;
    }

    public int getHeroInitialHp() {
        return 10000;
    }

    @Override
    public String toString() {
        return "Config{" +
                "小兵间隔:" + getMobCreate().getScaleNow() +
                ",精英间隔:" + getEliteCreate().getScaleNow() +
                ",敌机射击间隔:" + getEnemyShoot().getScaleNow() +
                ",首领射击间隔:" + getBossShoot().getScaleNow() +
                ",英雄射击间隔:" + getHeroShoot().getScaleNow() +
                ",首领产生分数间隔:" + getBossScoreThreshold().getScaleNow() +
                ",掉落概率:" + getDropPropsRate().getScaleNow() +
                ",敌机增强倍率:" + getEnemyMagnification().getScaleNow() +
                ",首领初始血量:" + getBossInitialHp().getScaleNow() +
                "}";
    }

    public void printNow() {
        Utils.getLogger().info(this.toString());
    }

    public int getHeroBoxSize() {
        return 12;
    }

    public double getAircraftCrashDecreaseHp() {
        return 1000;
    }

    public int getEliteJumpTime() {
        return 1000;
    }

    public Vec2 getEliteJumpRange() {
        return new Vec2(100, 300);
    }
}
