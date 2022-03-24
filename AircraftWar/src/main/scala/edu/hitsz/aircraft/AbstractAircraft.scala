package edu.hitsz.aircraft

import edu.hitsz.animate.AnimateContainer
import edu.hitsz.basic.PositionType.Position
import edu.hitsz.basic.{FlyingObject, Vec2Double}
import edu.hitsz.bullet.AbstractBullet

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author chiro2001
 */
abstract class AbstractAircraft(posInit: Position, animateContainer: AnimateContainer[Vec2Double], hpInit: Int)
  extends FlyingObject(posInit, animateContainer) {
  // 生命值
  var hp = hpInit

  def decreaseHp(decrease: Int) = {
    hp -= decrease
    if (hp <= 0) {
      hp = 0
      vanish()
    }
  }

  def getHp = hp

  /**
   * 飞机射击方法，可射击对象必须实现
   *
   * @return
   * 可射击对象需实现，返回子弹
   * 非可射击对象空实现，返回空 Seq
   */
  def shoot(): Seq[AbstractBullet]
}