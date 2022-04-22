package edu.hitsz.aircraft;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBulletFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.vector.Vec2;

import java.util.LinkedList;

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    /**
     * 子弹一次发射数量
     */
    private int shootNum = 10;

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
            // 多个子弹横向分散
            BaseBullet baseBullet = new HeroBulletFactory(getPosition().plus(new Vec2((i * 2 - shootNum + 1) * 10, 0))).create();
            res.add(baseBullet);
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
}
