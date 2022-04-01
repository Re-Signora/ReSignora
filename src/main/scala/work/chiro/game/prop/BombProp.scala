package work.chiro.game.prop

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft.AbstractAircraft
import work.chiro.game.animate.{AnimateContainer, AnimateLinearToTarget, AnimateVectorType}
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.ScaleType.Position
import work.chiro.game.logger
import work.chiro.game.utils.getTimeMills

class BombProp(posInit: Position, animateContainer: AnimateContainer)
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
      position, new AnimateContainer(List(
        new AnimateLinearToTarget(position, new Position(position.getX, config.window.playHeight),
          AnimateVectorType.PositionLike.id, getTimeMills, 20000)
      ))
    )
  }
}

