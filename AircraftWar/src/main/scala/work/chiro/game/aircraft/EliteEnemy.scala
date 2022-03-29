package work.chiro.game.aircraft

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.{AnimateContainer, AnimateLinearToTarget, AnimateNonLinearToTargetVec, AnimateVectorType}
import work.chiro.game.application.{ImageResourceFactory, Main}
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.{AbstractObjectFactory, Vec2Double}
import work.chiro.game.bullet.EnemyBullet
import work.chiro.game.utils.{getNewFlightPosition, getTimeMills}

/**
 * 普通敌机，不可攻击
 *
 * @author chiro2001
 * @param posInit          敌机初始位置
 * @param animateContainer 动画容器
 * @param hpInit           初始血量
 */
class EliteEnemy(posInit: Position, animateContainer: AnimateContainer[Vec2Double], hpInit: Int)
  extends AbstractAircraft(posInit, animateContainer, hpInit) {

  val power = 10

  override def forward() = {
    super.forward()
    // 判定 y 轴向下飞行出界
    if (getLocationY >= config.window.height) vanish()
  }

  override def shoot() = List({
    val posNew = new Position(getLocationX, getLocationY)
    new EnemyBullet(posNew, new AnimateContainer[Position](List(
      // new AnimateLinear(posNew, new Position(posNew.getX, Main.WINDOW_HEIGHT), AnimateVectorType.PositionLike.id, getTimeMills, 3000)
      // new AnimateLinearToTarget(posNew, HeroAircraft.getPositionInstance, AnimateVectorType.PositionLike.id, getTimeMills, 900, willStop = false)
      new AnimateNonLinearToTargetVec(posNew, HeroAircraft.getPositionInstance, AnimateVectorType.PositionLike.id, getTimeMills, 3000,
        0.1, 0.00006, softStop = true, willStop = false)
    )), power)
  })

  override def getImage = EliteEnemy.getImage
}

object EliteEnemy extends ImageResourceFactory with AbstractObjectFactory {
  override def getImageCachedPath = "images/elite.png"

  def create() = {
    val positionEnemyNew = getNewFlightPosition(EliteEnemy.getImage.getWidth)
    new EliteEnemy(
      positionEnemyNew, new AnimateContainer[Position](List(
        new AnimateLinearToTarget(positionEnemyNew, new Position(positionEnemyNew.getX, config.window.height),
          AnimateVectorType.PositionLike.id, getTimeMills, 20000)
      )), 30
    )
  }
}