package work.chiro.game.config;

import work.chiro.game.timer.TimerLinearChange;

/**
 * 配置参数类
 *
 * @author Chiro
 */
public abstract class AbstractConfig {
    protected TimerLinearChange mobCreate = new TimerLinearChange(700, -1e-2);
    protected TimerLinearChange eliteCreate = new TimerLinearChange(1200);
    protected TimerLinearChange enemyShoot = new TimerLinearChange(200);
    protected TimerLinearChange heroShoot = new TimerLinearChange(10);

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

    public void printNow() {
        System.out.println("Mob create: " + getMobCreate().getScaleNow());
    }
}
