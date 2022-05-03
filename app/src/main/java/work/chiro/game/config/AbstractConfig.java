package work.chiro.game.config;

import work.chiro.game.timer.TimerLinearChange;

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
    protected TimerLinearChange heroShoot = new TimerLinearChange(100d, 1e-3, 10d);
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
                "MobCreate:" + getMobCreate().getScaleNow() +
                ",EliteCreate:" + getEliteCreate().getScaleNow() +
                ",enemyShoot:" + getEnemyShoot().getScaleNow() +
                ",bossShoot:" + getBossShoot().getScaleNow() +
                ",heroShoot:" + getHeroShoot().getScaleNow() +
                ",bossScoreThreshold:" + getBossScoreThreshold().getScaleNow() +
                ",dropRate:" + getDropPropsRate().getScaleNow() +
                ",enemyMagnification:" + getEnemyMagnification().getScaleNow() +
                ",bossInitialHp:" + getBossInitialHp().getScaleNow() +
                "}";
    }

    public void printNow() {
        System.out.println(this);
    }
}
