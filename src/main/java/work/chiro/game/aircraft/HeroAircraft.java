package work.chiro.game.aircraft;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.bullet.HeroBulletFactory;
import work.chiro.game.prop.AbstractProp;
import work.chiro.game.vector.Vec2;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    public HeroAircraft(Vec2 posInit, AnimateContainer animateContainer, int hp) {
        super(posInit, animateContainer, hp, 0);
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
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
            BaseBullet baseBullet = new HeroBulletFactory(getPosition().copy(), i, List.of()).create();
            res.add(baseBullet);
        }
        return res;
    }

    public LinkedList<BaseBullet> shoot(List<List<? extends AbstractAircraft>> allEnemyAircrafts) {
        LinkedList<BaseBullet> res = new LinkedList<>();
        for (int i = 0; i < shootNum; i++) {
            res.addAll(new HeroBulletFactory(getPosition().copy(), i, allEnemyAircrafts).createMany());
        }
        return res;
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
}
