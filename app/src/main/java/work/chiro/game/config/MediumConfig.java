package work.chiro.game.config;

import work.chiro.game.timer.TimerLinearChange;

/**
 * @author Chiro
 */
public class MediumConfig extends AbstractConfig {
    protected TimerLinearChange mobCreate = new TimerLinearChange(700, 1e-3, 200d);
    protected TimerLinearChange eliteCreate = new TimerLinearChange(1200, 1e-3, 500d);
    protected TimerLinearChange enemyShoot = new TimerLinearChange(200, 1e-4, 100d);
    protected TimerLinearChange bossShoot = new TimerLinearChange(200, 1e-4, 100d);
    protected TimerLinearChange dropPropsRate = new TimerLinearChange(0.2);
    protected TimerLinearChange enemyMagnification = new TimerLinearChange(1.0, 1e-8, 3.0);
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
    public int getHeroInitialHp() {
        return 5000;
    }
}
