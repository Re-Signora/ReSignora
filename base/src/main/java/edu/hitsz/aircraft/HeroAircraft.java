package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBulletFactory;
import edu.hitsz.prop.AbstractProp;

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
    private int shootNum = 1;
    /**
     * 子弹伤害
     */
    private final int power = 30;
    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private final int direction = -1;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp        初始生命值
     */
    public HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, 0);
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    @Override
    public LinkedList<BaseBullet> shoot() {
        LinkedList<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction * 5;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            BaseBullet baseBullet = new HeroBulletFactory(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power).create();
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
