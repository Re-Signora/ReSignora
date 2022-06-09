package work.chiro.game.objects.aircraft;

import java.util.LinkedList;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.bullet.BaseBullet;
import work.chiro.game.objects.bullet.EnemyBulletFactory;
import work.chiro.game.x.compatible.colors.DrawColor;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.config.Constants;
import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.objects.prop.AbstractProp;
import work.chiro.game.objects.prop.BloodPropFactory;
import work.chiro.game.objects.prop.BombPropFactory;
import work.chiro.game.objects.prop.BulletPropFactory;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class BossEnemy extends AbstractAircraft {
    public BossEnemy(Vec2 posInit, AnimateContainer animateContainer, double hp) {
        super(posInit, animateContainer, hp, 500);
    }

    @Override
    public void vanish() {
        super.vanish();
        ResourceProvider.getInstance().stopMusic(MusicType.BGM_BOSS);
        ResourceProvider.getInstance().startMusic(MusicType.BGM);
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
        if (RunningConfig.difficulty == Difficulty.Medium || RunningConfig.difficulty == Difficulty.Hard) {
            ret.add(new EnemyBulletFactory(getPosition().copy(), EnemyBulletFactory.BulletType.Direct).create());
        }
        if (RunningConfig.difficulty == Difficulty.Hard) {
            ret.add(new EnemyBulletFactory(getPosition().copy(), EnemyBulletFactory.BulletType.Scatter).create());
        }
        return ret;
    }

    @Override
    public LinkedList<AbstractProp> dropProps() {
        LinkedList<AbstractProp> props = new LinkedList<>();
        double range = Constants.BOSS_DROP_RANGE;
        props.add(new BloodPropFactory(getPosition().plus(Utils.randomPosition(new Vec2(-range, 0), new Vec2(range, range)))).create());
        props.add(new BombPropFactory(getPosition().plus(Utils.randomPosition(new Vec2(-range, 0), new Vec2(range, range)))).create());
        props.add(new BulletPropFactory(getPosition().plus(Utils.randomPosition(new Vec2(-range, 0), new Vec2(range, range)))).create());
        return props;
    }

    @Override
    protected void drawHp(XGraphics g) {
        int hpBarHeight = Constants.DRAW_HP_BAR * 3 / 2;
        g.setColor(DrawColor.gray)
                .fillRect(0, 0, RunningConfig.windowWidth, hpBarHeight)
                .setColor(DrawColor.red)
                .fillRect(0, 0, (double) RunningConfig.windowWidth / maxHp * getHp(), hpBarHeight);
    }
}
