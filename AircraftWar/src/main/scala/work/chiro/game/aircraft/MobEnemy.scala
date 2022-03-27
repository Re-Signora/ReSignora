package work.chiro.game.aircraft

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.{AnimateContainer, AnimateLinearToTarget, AnimateVectorType}
import work.chiro.game.application.{ImageResource, Main}
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double
import work.chiro.game.utils.{getNewFlightPosition, getTimeMills}

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
    if (getLocationY >= config.window.height) vanish()
  }

  override def shoot() = List()

  override def getImage = MobEnemy.getImage

  override def create() = MobEnemy.create()
}

object MobEnemy extends ImageResource {
  override def getImageCachedPath = "images/mob.png"
  def create() = {
    val positionEnemyNew = getNewFlightPosition(MobEnemy.getImage.getWidth)
    new MobEnemy(
      positionEnemyNew, new AnimateContainer[Position](List(
        new AnimateLinearToTarget(positionEnemyNew, new Position(positionEnemyNew.getX, config.window.height),
          AnimateVectorType.PositionLike.id, getTimeMills, 2000)
      )), 30
    )
  }
}