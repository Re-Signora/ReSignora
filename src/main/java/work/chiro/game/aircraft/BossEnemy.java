package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.MusicManager;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.bullet.EnemyBulletFactory;
import work.chiro.game.prop.AbstractProp;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

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
    public LinkedList<BaseBullet> shoot() {
        LinkedList<BaseBullet> ret = new LinkedList<>();
        ret.add(new EnemyBulletFactory(getPosition().copy(), EnemyBulletFactory.BulletType.Tracking).create());
        return ret;
    }

    @Override
    public LinkedList<AbstractProp> dropProps() {
        return new LinkedList<>();
    }
}
