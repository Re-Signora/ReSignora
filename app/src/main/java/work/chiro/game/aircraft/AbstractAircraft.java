package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.MusicManager;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.config.Constants;
import work.chiro.game.prop.AbstractProp;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;
    protected int score;
    static private double lastPlayMusic = 0;

    public int getScore() {
        return score;
    }

    public AbstractAircraft(
            Vec2 posInit,
            AnimateContainer animateContainer,
            int hp,
            int score) {
        super(posInit, animateContainer);
        this.hp = hp;
        this.maxHp = hp;
        this.score = score;
    }

    public void decreaseHp(int decrease) {
        hp -= decrease;
        if (hp <= 0) {
            hp = 0;
            vanish();
        }
        playBeShootMusic();
    }

    protected void playBeShootMusic() {
        double now = Utils.getTimeMills();
        if (now - lastPlayMusic > Constants.MUSIC_SHOOT_MIN_CYCLE_MS) {
            Utils.startMusic(MusicManager.MusicType.HERO_HIT);
            lastPlayMusic = now;
        }
    }

    public int getHp() {
        return hp;
    }


    /**
     * 飞机射击方法，可射击对象必须实现
     *
     * @return 可射击对象需实现，返回子弹
     * 非可射击对象空实现，返回null
     */
    public abstract LinkedList<BaseBullet> shoot();

    /**
     * 飞机掉落道具
     *
     * @return 返回所有的道具
     */
    public abstract LinkedList<AbstractProp> dropProps();
}


