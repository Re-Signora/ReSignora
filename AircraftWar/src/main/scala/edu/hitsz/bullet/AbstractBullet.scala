package edu.hitsz.bullet

import edu.hitsz.animate.AnimateContainer
import edu.hitsz.application.Main
import edu.hitsz.basic.PositionType.Position
import edu.hitsz.basic.{FlyingObject, Vec2Double}

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author chiro2001
 */
abstract class AbstractBullet(posInit: Position, animateContainer: AnimateContainer[Vec2Double], power: Int)
  extends FlyingObject(posInit, animateContainer) {
  override def forward() = {
    super.forward()
    // 判定 x 轴出界
    if (getLocationX <= 0 || getLocationY >= Main.WINDOW_WIDTH) vanish()
    // 判定 y 轴出界
    if (getSpeedY > 0 && getLocationY >= Main.WINDOW_HEIGHT) {
      // 向下飞行出界
      vanish()
    }
    else if (getLocationY <= 0) {
      // 向上飞行出界
      vanish()
    }
  }

  def getPower = power
}