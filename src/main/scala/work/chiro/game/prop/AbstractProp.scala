package work.chiro.game.prop

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft.AbstractAircraft
import work.chiro.game.animate.AnimateContainer
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.{AbstractObject, Vec2Double}

abstract class AbstractProp(posInit: Position, animateContainer: AnimateContainer[Vec2Double])
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
  }

  def handleAircrafts(enemyAircrafts: List[AbstractAircraft]): Unit

  def create(position: Position): AbstractProp
}