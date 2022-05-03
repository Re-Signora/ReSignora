package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.prop.AbstractProp;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {
    public MobEnemy(AbstractConfig config, Vec2 posInit, AnimateContainer animateContainer, int hp) {
        super(config, posInit, animateContainer, hp, 30);
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public LinkedList<AbstractProp> dropProps(AbstractConfig config) {
        return new LinkedList<>();
    }

}
