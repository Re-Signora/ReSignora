package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.MusicManager;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.bullet.HeroBulletFactory;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.Constants;
import work.chiro.game.prop.AbstractProp;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    private double heroLastPlayMusic = 0;
    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;
    final private AircraftHeroBox box;

    public HeroAircraft(Vec2 posInit, AnimateContainer animateContainer, int hp) {
        super(posInit, animateContainer, hp, 0);
        box = new AircraftHeroBox(posInit, new Vec2(12, 12));
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    /**
     * 通过射击产生子弹
     *
     * @return 射击出的子弹List
     */
    @Override
    public LinkedList<BaseBullet> shoot() {
        LinkedList<BaseBullet> res = new LinkedList<>();
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            BaseBullet baseBullet = new HeroBulletFactory(getPosition().copy(), List.of()).create();
            res.add(baseBullet);
        }
        return res;
    }

    public LinkedList<BaseBullet> shoot(List<List<? extends AbstractAircraft>> allEnemyAircrafts) {
        return new LinkedList<>(new HeroBulletFactory(getPosition(), allEnemyAircrafts).createMany(getShootNum()));
    }

    @Override
    public LinkedList<AbstractProp> dropProps(AbstractConfig config) {
        return new LinkedList<>();
    }

    public void increaseShootNum() {
        shootNum++;
    }

    public void decreaseShootNum() {
        shootNum = Math.max(1, shootNum - 1);
    }

    public int getShootNum() {
        return shootNum;
    }

    @Override
    public boolean crash(AbstractFlyingObject abstractFlyingObject) {
        return box.crash(abstractFlyingObject);
    }

    @Override
    protected void drawHp(Graphics g) {
        super.drawHp(g, Color.green, Color.lightGray);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        box.draw(g);
    }

    @Override
    public void vanish() {
        // 保持存在
    }

    @Override
    protected void playBeShootMusic() {
        double now = Utils.getTimeMills();
        if (now - heroLastPlayMusic > Constants.MUSIC_HERO_HURT_MIN_CYCLE_MS) {
            Utils.startMusic(MusicManager.MusicType.HERO_SHOOT);
            heroLastPlayMusic = now;
        }
    }
}
