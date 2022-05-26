package work.chiro.game.objects.prop;

import java.util.LinkedList;
import java.util.List;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.objects.aircraft.AbstractAircraft;
import work.chiro.game.objects.bullet.BaseBullet;
import work.chiro.game.resource.MusicType;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.ResourceProvider;

/**
 * 道具类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
abstract public class AbstractProp extends AbstractFlyingObject<AnimateContainer> {
    protected List<AbstractAircraft> enemyAircrafts = new LinkedList<>();
    protected List<BaseBullet> enemyBullets = new LinkedList<>();

    public AbstractProp(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    protected void playSupplyMusic() {
        ResourceProvider.getInstance().startMusic(MusicType.PROPS);
    }

    public AbstractProp subscribeEnemyAircrafts(List<AbstractAircraft> enemyAircrafts) {
        this.enemyAircrafts = enemyAircrafts;
        return this;
    }

    public AbstractProp subscribeEnemyBullets(List<BaseBullet> enemyBullets) {
        this.enemyBullets = enemyBullets;
        return this;
    }

    /**
     * 英雄机获取到道具时候的处理函数
     *
     * @return this
     */
    abstract public AbstractProp update();
}
