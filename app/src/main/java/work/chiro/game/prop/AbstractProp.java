package work.chiro.game.prop;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.MusicManager;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

import java.util.List;

/**
 * 道具类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
abstract public class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    protected void playSupplyMusic() {
        Utils.startMusic(MusicManager.MusicType.PROPS);
    }

    /**
     * 处理飞机碰到道具的时候的反应
     *
     * @param enemyAircrafts 所有的敌机
     */
    abstract public void handleAircrafts(List<AbstractAircraft> enemyAircrafts);
}
