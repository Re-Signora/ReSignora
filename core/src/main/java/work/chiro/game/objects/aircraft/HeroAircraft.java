package work.chiro.game.objects.aircraft;

import java.util.LinkedList;
import java.util.List;

import work.chiro.game.animate.Animate;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.AbstractObject;
import work.chiro.game.objects.bullet.BaseBullet;
import work.chiro.game.objects.bullet.HeroBulletFactory;
import work.chiro.game.x.compatible.DrawColor;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.config.Constants;
import work.chiro.game.objects.prop.AbstractProp;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

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

    public HeroAircraft(Vec2 posInit, AnimateContainer animateContainer, Vec2 boxSize, double hp) {
        super(posInit, animateContainer, hp, 0, new Scale(1));
        box = new AircraftHeroBox(posInit, boxSize);
        startInvincibleState();
    }

    public void startInvincibleState() {
        if (box.isInvincible()) {
            return;
        }
        box.setInvincible(true);
        getAnimateContainer().clearAllAnimates();
        getAnimateContainer().addAnimate(new Animate.Delay<>(new Vec2(), 50));
        getAnimateContainer().setAnimateCallback(animateContainer -> {
            synchronized (HeroAircraft.class) {
                animateContainer.clearAllAnimates();
                animateContainer.addAnimate(new Animate.Delay<>(new Vec2(), 50));
                if (getAlpha().getX() > Constants.HERO_ALPHA_DATA.get(1)) {
                    getAlpha().setX(Constants.HERO_ALPHA_DATA.get(0));
                } else {
                    getAlpha().setX(Constants.HERO_ALPHA_DATA.get(2));
                }
            }
            return false;
        });
        MyThreadFactory.getInstance().newThread(() -> {
            try {
                Thread.sleep(Constants.INVINCIBLE_TIME);
                getAnimateContainer().clearAnimateCallback();
                getAnimateContainer().clearAllAnimates();
                Thread.sleep(Constants.INVINCIBLE_TIME / 10);
            } catch (InterruptedException ignored) {
            }
            finally {
                synchronized (HeroAircraft.class) {
                    getAlpha().setX(1);
                    box.setInvincible(false);
                }
            }
        }).start();
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，forward 不能控制位置
        getAnimateContainer().updateAll(Utils.getTimeMills());
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
    public LinkedList<AbstractProp> dropProps() {
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
    public boolean crash(AbstractObject abstractObject) {
        return box.crash(abstractObject);
    }

    @Override
    protected void drawHp(XGraphics g) {
        super.drawHp(g, DrawColor.green, DrawColor.gray, true);
    }

    @Override
    public void draw(XGraphics g) {
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
            ResourceProvider.getInstance().startMusic(MusicType.HERO_SHOOT);
            heroLastPlayMusic = now;
        }
    }
}
