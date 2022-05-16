package work.chiro.game.aircraft;

import java.util.LinkedList;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.prop.AbstractProp;
import work.chiro.game.prop.BloodPropFactory;
import work.chiro.game.prop.BombPropFactory;
import work.chiro.game.prop.BulletPropFactory;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * 精英敌机，可以射击
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractAircraft {

    public EliteEnemy(AbstractConfig config, Vec2 posInit, AnimateContainer animateContainer, double hp) {
        super(config, posInit, animateContainer, hp, 100);
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        return Utils.letEnemyShoot(config, getPosition().copy());
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @Override
    public LinkedList<AbstractProp> dropProps() {
        double select = Utils.getRandom().nextDouble();
        config.getDropPropsRate().update(Utils.getTimeMills());
        double probability = config.getDropPropsRate().getScaleNow().getX();
        LinkedList <AbstractProp> props = new LinkedList<>();
        if (Utils.isInRange(select, 0, probability)) {
            props.add(new BloodPropFactory(config, getPosition().copy()).create());
        } else if (Utils.isInRange(select, probability, probability * 2)) {
            props.add(new BombPropFactory(config, getPosition().copy()).create());
        } else if (Utils.isInRange(select, probability * 2, probability * 3)) {
            props.add(new BulletPropFactory(config, getPosition().copy()).create());
        }
        // else no props.
        return props;
    }
}
