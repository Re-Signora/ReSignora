package work.chiro.game.bullet

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.AnimateContainer
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.{AbstractObject, Vec2Double}

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author chiro2001
 */
abstract class AbstractBullet(posInit: Position, animateContainer: AnimateContainer[Vec2Double], power: Int)
  extends AbstractObject(posInit, animateContainer) {
  override def forward() = {
    super.forward()
    // 判定 x 轴出界
    if (getLocationX <= 0 || getLocationX >= config.window.playWidth) vanish()
    // 判定 y 轴出界
    if (getLocationY <= 0 || getLocationY >= config.window.playHeight) vanish()
    // if (getSpeedY > 0 && getLocationY >= config.window.playHeight) {
    //   // 向下飞行出界
    //   vanish()
    // }
    // else if (getLocationY <= 0) {
    //   // 向上飞行出界
    //   vanish()
    // }
  }

  def getPower = power
}