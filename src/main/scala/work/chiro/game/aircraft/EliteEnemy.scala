package work.chiro.game.aircraft

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.{AnimateContainer, AnimateLinear, AnimateNonLinear, AnimateVectorType}
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.PositionType.{Position, Scale, Size, SizeDouble}
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
class EliteEnemy(posInit: Position,
                 animateContainer: AnimateContainer,
                 hpInit: Int,
                 sizeInit: Option[SizeDouble] = None,
                 rotationInit: Option[Scale] = None)
  extends AbstractAircraft(posInit, animateContainer, hpInit, sizeInit = sizeInit, rotationInit = rotationInit) {

  val power = 10

  override def forward() = {
    super.forward()
    // 判定 y 轴向下飞行出界
    if (getLocationY >= config.window.playHeight) vanish()
  }

  override def shoot() = List({
    val posNew = new Position(getLocationX, getLocationY)
    new EnemyBullet(posNew, new AnimateContainer(List(
      new AnimateNonLinear(posNew, HeroAircraft.getPositionInstance, AnimateVectorType.PositionLike.id, getTimeMills, 3000, willStop = false)
    )), power)
  })

  override def getImage = EliteEnemy.getImage
}

object EliteEnemy extends ImageResourceFactory with AbstractObjectFactory {
  override def getImageCachedPath = "images/elite.png"

  def create() = {
    val positionEnemyNew = getNewFlightPosition(EliteEnemy.getImage.getWidth)
    val newRotation = new Scale
    new EliteEnemy(
      positionEnemyNew, new AnimateContainer(List(
        new AnimateLinear(positionEnemyNew, new Position(0, 0.1),
          AnimateVectorType.PositionLike.id, getTimeMills, 0)
      )), 30, rotationInit = Some(newRotation)
    )
  }
}
