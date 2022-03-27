package work.chiro.game.aircraft

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.{AbstractObject, Vec2Double}
import work.chiro.game.bullet.AbstractBullet

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author chiro2001
 */
abstract class AbstractAircraft(posInit: Position, animateContainer: AnimateContainer[Vec2Double], hpInit: Int)
  extends AbstractObject(posInit, animateContainer) {
  // 生命值
  var hp = hpInit
  val hpMax = 300

  def decreaseHp(decrease: Int) = {
    hp -= decrease
    if (hp <= 0) {
      hp = 0
      vanish()
    }
  }

  def increaseHp(increase: Int) = {
    hp = math.min(hp + increase, hpMax)
  }

  def getHp = hp

  /**
   * 飞机射击方法，可射击对象必须实现
   *
   * @return
   * 可射击对象需实现，返回子弹 <br>
   * 非可射击对象空实现，返回空 Seq
   */
  def shoot(): Seq[AbstractBullet]
}