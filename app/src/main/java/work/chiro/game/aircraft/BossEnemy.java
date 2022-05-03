package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.MusicManager;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.bullet.EnemyBulletFactory;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.Constants;
import work.chiro.game.prop.AbstractProp;
import work.chiro.game.prop.BloodPropFactory;
import work.chiro.game.prop.BombPropFactory;
import work.chiro.game.prop.BulletPropFactory;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

import java.awt.*;
import java.util.LinkedList;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class BossEnemy extends AbstractAircraft {
    public BossEnemy(Vec2 posInit, AnimateContainer animateContainer, int hp) {
        super(posInit, animateContainer, hp, 500);
    }

    @Override
    public void vanish() {
        super.vanish();
        Utils.stopMusic(MusicManager.MusicType.BGM_BOSS);
        Utils.startMusic(MusicManager.MusicType.BGM);
        BossEnemyFactory.clearInstance();
    }

    @Override
    protected Boolean checkInBoundary() {
        return true;
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        LinkedList<BaseBullet> ret = new LinkedList<>();
        ret.add(new EnemyBulletFactory(getPosition().copy(), EnemyBulletFactory.BulletType.Tracking).create());
        return ret;
    }

    @Override
    public LinkedList<AbstractProp> dropProps(AbstractConfig config) {
        LinkedList<AbstractProp> props = new LinkedList<>();
        double range = Constants.BOSS_DROP_RANGE;
        props.add(new BloodPropFactory(getPosition().plus(Utils.randomPosition(new Vec2(-range, 0), new Vec2(range, range)))).create());
        props.add(new BombPropFactory(getPosition().plus(Utils.randomPosition(new Vec2(-range, 0), new Vec2(range, range)))).create());
        props.add(new BulletPropFactory(getPosition().plus(Utils.randomPosition(new Vec2(-range, 0), new Vec2(range, range)))).create());
        return props;
    }

    @Override
    protected void drawHp(Graphics g) {
        int hpBarHeight = Constants.DRAW_HP_BAR * 3 / 2;
        g.setColor(Color.gray);
        g.fillRect(0, 0, Constants.WINDOW_WIDTH, hpBarHeight);
        g.setColor(Color.red);
        g.fillRect(0, 0, (int) ((double) (Constants.WINDOW_WIDTH) / maxHp * getHp()), hpBarHeight);
    }
}
