package work.chiro.game.prop;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.MusicManager;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;
import java.util.List;

/**
 * 道具类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
abstract public class AbstractProp extends AbstractFlyingObject implements PropApplier {
    protected List<AbstractAircraft> enemyAircrafts = new LinkedList<>();
    protected List<BaseBullet> enemyBullets = new LinkedList<>();

    public AbstractProp(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    protected void playSupplyMusic() {
        Utils.startMusic(MusicManager.MusicType.PROPS);
    }

    public AbstractProp subscribeEnemyAircrafts(List<AbstractAircraft> enemyAircrafts) {
        this.enemyAircrafts = enemyAircrafts;
        return this;
    }

    public AbstractProp subscribeEnemyBullets(List<BaseBullet> enemyBullets) {
        this.enemyBullets = enemyBullets;
        return this;
    }
}
