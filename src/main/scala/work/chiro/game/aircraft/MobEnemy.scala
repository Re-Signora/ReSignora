package work.chiro.game.aircraft

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.{AnimateContainer, AnimateLinear, AnimateVectorType}
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.PositionType.{Position, Scale, Size, SizeDouble}
import work.chiro.game.basic.{AbstractObjectFactory, Vec2Double, VecDouble}
import work.chiro.game.utils.{getNewFlightPosition, getTimeMills}

/**
 * 普通敌机，不可攻击
 *
 * @author chiro2001
 * @param posInit          敌机初始位置
 * @param animateContainer 动画容器
 * @param hpInit           初始血量
 */
class MobEnemy(posInit: Position,
               animateContainer: AnimateContainer,
               hpInit: Int,
               sizeInit: Option[SizeDouble] = None,
               rotationInit: Option[Scale] = None)
  extends AbstractAircraft(posInit, animateContainer, hpInit, sizeInit = sizeInit, rotationInit = rotationInit) {
  override def forward() = {
    super.forward()
    // 判定 y 轴向下飞行出界
    if (getLocationY >= config.window.playHeight) vanish()
  }

  override def shoot() = List()

  override def getImage = MobEnemy.getImage
}

object MobEnemy extends ImageResourceFactory with AbstractObjectFactory {
  override def getImageCachedPath = "images/mob.png"

  def create() = {
    val positionEnemyNew = getNewFlightPosition(MobEnemy.getImage.getWidth)
    val newSize = new SizeDouble(MobEnemy.getImage.getWidth, MobEnemy.getImage.getHeight)
    new MobEnemy(
      positionEnemyNew, new AnimateContainer(List(
        new AnimateLinear(positionEnemyNew, new Position(0, 0.2),
          AnimateVectorType.PositionLike.id, getTimeMills, 0),
        new AnimateLinear(newSize, new SizeDouble(0.05, 0.05), AnimateVectorType.Others.id, getTimeMills, 0)
      )), 30, sizeInit = Some(newSize)
    )
  }
}