package work.chiro.game.objects.aircraft;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.bullet.BaseBullet;
import work.chiro.game.objects.prop.AbstractProp;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {
    public MobEnemy(Vec2 posInit, AnimateContainer animateContainer, double hp) {
        super(posInit, animateContainer, hp, 30);
    }

    @Override
    public LinkedList<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public LinkedList<AbstractProp> dropProps() {
        return new LinkedList<>();
    }

}
