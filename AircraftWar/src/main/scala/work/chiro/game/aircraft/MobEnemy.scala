package work.chiro.game.aircraft

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.application.{ImageResource, Main}
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double

/**
 * 普通敌机，不可攻击
 *
 * @author chiro2001
 * @param posInit          敌机初始位置
 * @param animateContainer 动画容器
 * @param hpInit           初始血量
 */
class MobEnemy(posInit: Position, animateContainer: AnimateContainer[Vec2Double], hpInit: Int)
  extends AbstractAircraft(posInit, animateContainer, hpInit) {
  override def forward() = {
    super.forward()
    // 判定 y 轴向下飞行出界
    if (getLocationY >= Main.WINDOW_HEIGHT) vanish()
  }

  override def shoot() = List()

  override def getImage = MobEnemy.getImage
}

object MobEnemy extends ImageResource {
  override def getImageCachedPath = "images/mob.png"
}