package work.chiro.game.objects.aircraft;

import java.util.LinkedList;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.config.Constants;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.objects.bullet.BaseBullet;
import work.chiro.game.objects.prop.AbstractProp;
import work.chiro.game.objects.prop.PropHandler;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.colors.DrawColor;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XGraphics;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft
        extends AbstractFlyingObject<AnimateContainer>
        implements PropHandler {
    /**
     * 生命值
     */
    protected double maxHp;
    protected double hp;
    protected double score;
    static private double lastPlayMusic = 0;

    public double getScore() {
        return score;
    }

    public AbstractAircraft(
            Vec2 posInit,
            AnimateContainer animateContainer,
            double hp,
            double score,
            Scale alpha) {
        super(posInit, animateContainer, alpha);
        this.hp = hp;
        this.maxHp = hp;
        this.score = score;
    }

    public AbstractAircraft(
            Vec2 posInit,
            AnimateContainer animateContainer,
            double hp,
            double score) {
        this(posInit, animateContainer, hp, score, null);
    }

    public void decreaseHp(double decrease) {
        hp -= decrease;
        if (hp <= 0) {
            hp = 0;
            vanish(true);
        }
        if (decrease > 0) {
            playBeShootMusic();
        } else {
            maxHp = Math.max(maxHp, hp);
        }
    }

    protected void playBeShootMusic() {
        double now = TimeManager.getTimeMills();
        if (now - lastPlayMusic > Constants.MUSIC_SHOOT_MIN_CYCLE_MS) {
            ResourceProvider.getInstance().startMusic(MusicType.HERO_HIT);
            lastPlayMusic = now;
        }
    }

    public double getHp() {
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

    protected void drawHp(XGraphics g, int colorFront, int colorBack, boolean forceDraw) {
        if (getHp() == maxHp && !forceDraw) {
            return;
        }
        int hpBarHeight = RunningConfig.drawHpBar;
        g.setColor(colorBack)
                .fillRect(getLocationX() - getWidth() / 2, getLocationY() - getHeight() / 2,
                        getWidth(), hpBarHeight)
                .setColor(colorFront)
                .fillRect(getLocationX() - getWidth() / 2, getLocationY() - getHeight() / 2,
                        getWidth() / maxHp * getHp(), hpBarHeight);
    }

    protected void drawHp(XGraphics g) {
        drawHp(g, DrawColor.red, DrawColor.gray, false);
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
        drawHp(g);
    }

    @Override
    public void onPropHandle() {
        vanish(true);
    }

    public void vanish(boolean increaseScore) {
        if (increaseScore) {
            RunningConfig.increaseScore(getScore());
        }
        vanish();
    }
}


