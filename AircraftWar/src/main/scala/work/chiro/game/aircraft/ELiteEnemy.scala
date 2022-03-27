package work.chiro.game.aircraft

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.{AnimateContainer, AnimateLinearToTarget, AnimateNonLinearToTarget, AnimateVectorType}
import work.chiro.game.application.{ImageResource, Main}
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double
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
class ELiteEnemy(posInit: Position, animateContainer: AnimateContainer[Vec2Double], hpInit: Int)
  extends AbstractAircraft(posInit, animateContainer, hpInit) {

  val power = 10

  override def forward() = {
    super.forward()
    // 判定 y 轴向下飞行出界
    if (getLocationY >= config.window.height) vanish()
  }

  override def shoot() = List({
    val x = getLocationX
    val y = getLocationY
    val posNew = new Position(x, y)
    new EnemyBullet(posNew, new AnimateContainer[Position](List(
      // new AnimateLinear(posNew, new Position(posNew.getX, Main.WINDOW_HEIGHT), AnimateVectorType.PositionLike.id, getTimeMills, 3000)
      // new AnimateLinearToTarget(posNew, HeroAircraft.getHeroPositionInstance, AnimateVectorType.PositionLike.id, getTimeMills, 3000)
      new AnimateNonLinearToTarget(posNew, HeroAircraft.getHeroPositionInstance, AnimateVectorType.PositionLike.id, getTimeMills, 3000,
        new Position(0, 0), new Position(0.002, 0.002))
    )), power)
  })

  override def getImage = ELiteEnemy.getImage

  override def create() = ELiteEnemy.create()
}

object ELiteEnemy extends ImageResource {
  override def getImageCachedPath = "images/elite.png"

  def create() = {
    val positionEnemyNew = getNewFlightPosition(ELiteEnemy.getImage.getWidth)
    new ELiteEnemy(
      positionEnemyNew, new AnimateContainer[Position](List(
        new AnimateLinearToTarget(positionEnemyNew, new Position(positionEnemyNew.getX, config.window.height),
          AnimateVectorType.PositionLike.id, getTimeMills, 20000)
      )), 30
    )
  }
}
