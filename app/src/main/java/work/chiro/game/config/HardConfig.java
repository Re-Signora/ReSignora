package work.chiro.game.config;

import work.chiro.game.timer.TimerLinearChange;

/**
 * @author Chiro
 */
public class HardConfig extends AbstractConfig {
    protected TimerLinearChange mobCreate = new TimerLinearChange(500, 1e-3, 100d);
    protected TimerLinearChange eliteCreate = new TimerLinearChange(800, 1e-3, 300d);
    protected TimerLinearChange enemyShoot = new TimerLinearChange(200, 1e-4, 80d);
    protected TimerLinearChange bossShoot = new TimerLinearChange(200, 1e-4, 80d);
    protected TimerLinearChange dropPropsRate = new TimerLinearChange(0.15, 1e-8, 0.08);
    protected TimerLinearChange enemyMagnification = new TimerLinearChange(1.5, 1e-8, 5.0);
    protected TimerLinearChange bossScoreThreshold = new TimerLinearChange(1000, 1e-3, 300d);
    protected TimerLinearChange bossInitialHp = new TimerLinearChange(8000, 1e-1, 12000d);
    protected TimerLinearChange heroShoot = new TimerLinearChange(30d, 1e-3, 50d);

    @Override
    public TimerLinearChange getMobCreate() {
        return mobCreate;
    }

    @Override
    public TimerLinearChange getEliteCreate() {
        return eliteCreate;
    }

    @Override
    public TimerLinearChange getEnemyShoot() {
        return enemyShoot;
    }

    @Override
    public TimerLinearChange getBossShoot() {
        return bossShoot;
    }

    @Override
    public TimerLinearChange getDropPropsRate() {
        return dropPropsRate;
    }

    @Override
    public TimerLinearChange getEnemyMagnification() {
        return enemyMagnification;
    }

    @Override
    public TimerLinearChange getBossScoreThreshold() {
        return bossScoreThreshold;
    }

    @Override
    public TimerLinearChange getBossInitialHp() {
        return bossInitialHp;
    }

    @Override
    public int getHeroInitialHp() {
        return 3000;
    }

    @Override
    public TimerLinearChange getHeroShoot() {
        return heroShoot;
    }
}
