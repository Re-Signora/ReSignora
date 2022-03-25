package edu.hitsz.aircraft

import edu.hitsz.animate.{AnimateContainer, AnimateLinear, AnimateVectorType}
import edu.hitsz.application.{ImageResource, Main}
import edu.hitsz.basic.PositionType.Position
import edu.hitsz.basic.Vec2Double
import edu.hitsz.bullet.HeroBullet
import edu.hitsz.utils.getTimeMills

/**
 * 英雄飞机，游戏玩家操控
 * @param posInit           初始坐标
 * @param animateContainer  动画容器
 * @param hpInit            初始血量
 */
class HeroAircraft(posInit: Position, animateContainer: AnimateContainer[Vec2Double], hpInit: Int)
  extends AbstractAircraft(posInit, animateContainer, hpInit) {
  // 攻击方式
  private val shootNum = 1 //子弹一次发射数量
  private val power = 30 //子弹伤害
  // private val direction = -1 //子弹射击方向 (向上发射：1，向下发射：-1)

  override def forward() = {
    // 英雄机由鼠标控制，不通过forward函数移动
  }

  /**
   * 通过射击产生子弹
   *
   * @return 射击出的子弹List
   */
  override def shoot() = {
    val x = getLocationX
    val y = getLocationY// + direction * 2
    // val speedX = 0
    // val speedY = getSpeedY + direction * 5
    for {i <- 0 until shootNum} yield {
      val posNew = new Position(x + (i * 2 - shootNum + 1) * 10, y)
      new HeroBullet(posNew, new AnimateContainer[Position](List(
        new AnimateLinear(posNew, new Position(posNew.getX, 0), AnimateVectorType.PositionLike.id, getTimeMills, 1000)
      )), power)
    }
    // for {i <- 0 until shootNum} yield new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power)
  }

  override def getImage = HeroAircraft.getImage
}

object HeroAircraft extends ImageResource {
  override def getImageCachedPath = "images/hero.png"
}