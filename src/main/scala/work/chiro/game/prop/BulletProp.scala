package work.chiro.game.prop

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft.{AbstractAircraft, HeroAircraft}
import work.chiro.game.animate.{AnimateContainer, AnimateLinearToTarget, AnimateVectorType}
import work.chiro.game.application.{ImageResourceFactory, Main}
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double
import work.chiro.game.logger
import work.chiro.game.utils.getTimeMills

class BulletProp(posInit: Position, animateContainer: AnimateContainer[Vec2Double])
  extends AbstractProp(posInit, animateContainer) {
  val bloodIncrease = 100

  override def handleAircrafts(enemyAircrafts: List[AbstractAircraft]) = {
    logger.info("FireSupply active!")
    HeroAircraft.getInstance.increasePower()
  }

  override def create(position: Position) = BulletProp.create(position)

  override def getImage = BulletProp.getImage
}

object BulletProp extends ImageResourceFactory {
  override def getImageCachedPath = "images/prop_bullet.png"

  def create(position: Position) = {
    new BulletProp(
      position, new AnimateContainer[Position](List(
        new AnimateLinearToTarget(position, new Position(position.getX, config.window.playHeight),
          AnimateVectorType.PositionLike.id, getTimeMills, 20000)
      ))
    )
  }
}




