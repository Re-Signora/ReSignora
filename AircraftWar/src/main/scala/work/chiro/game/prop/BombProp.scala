package work.chiro.game.prop

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft.{AbstractAircraft, HeroAircraft}
import work.chiro.game.animate.{AnimateContainer, AnimateLinearToTarget, AnimateVectorType}
import work.chiro.game.application.{ImageResourceFactory, Main}
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double
import work.chiro.game.logger
import work.chiro.game.utils.getTimeMills

class BombProp(posInit: Position, animateContainer: AnimateContainer[Vec2Double])
  extends AbstractProp(posInit, animateContainer) {
  override def handleAircrafts(enemyAircrafts: List[AbstractAircraft]) = {
    logger.info("BombSupply active!")
    enemyAircrafts.foreach(_.vanish())
  }

  override def create(position: Position) = BombProp.create(position)

  override def getImage = BombProp.getImage
}

object BombProp extends ImageResourceFactory {
  override def getImageCachedPath = "images/prop_bomb.png"

  def create(position: Position) = {
    new BombProp(
      position, new AnimateContainer[Position](List(
        new AnimateLinearToTarget(position, new Position(position.getX, config.window.height),
          AnimateVectorType.PositionLike.id, getTimeMills, 20000)
      ))
    )
  }
}

