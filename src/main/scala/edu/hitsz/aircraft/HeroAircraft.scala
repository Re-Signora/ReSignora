package edu.hitsz.aircraft

import edu.hitsz.bullet.{AbstractBullet, HeroBullet}

import java.util

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
class HeroAircraft(locationX: Int, locationY: Int, speedX: Int, speedY: Int, hpInit: Int)

/**
 * @param locationX 英雄机位置x坐标
 * @param locationY 英雄机位置y坐标
 * @param speedX    英雄机射出的子弹的基准速度（英雄机无特定速度）
 * @param speedY    英雄机射出的子弹的基准速度（英雄机无特定速度）
 * @param hpInit    初始生命值
 */ extends AbstractAircraft(locationX, locationY, speedX, speedY, hpInit) {
  /** 攻击方式 */
  private val shootNum = 1 //子弹一次发射数量
  private val power = 30 //子弹伤害
  private val direction = -1 //子弹射击方向 (向上发射：1，向下发射：-1)

  override def forward() = {
    // 英雄机由鼠标控制，不通过forward函数移动
  }

  override /*
      通过射击产生子弹
      @return 射击出的子弹List
     */ def shoot() = {
    val x = this.getLocationX
    val y = this.getLocationY + direction * 2
    val speedX = 0
    val speedY = this.getSpeedY + direction * 5
    var abstractBullet: HeroBullet = null
    for {i <- 0 until shootNum} yield new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power)
  }
}