package work.chiro.game.prop

import work.chiro.game.aircraft.{AbstractAircraft, HeroAircraft}
import work.chiro.game.animate.{AnimateContainer, AnimateLinear, AnimateVectorType}
import work.chiro.game.application.{ImageResource, Main}
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double
import work.chiro.game.utils.getTimeMills

class BombProp(posInit: Position, animateContainer: AnimateContainer[Vec2Double])
  extends AbstractProp(posInit, animateContainer) {
  override def handleAircrafts(enemyAircrafts: List[AbstractAircraft]) = {
    println("BombSupply active!")
    enemyAircrafts.foreach(_.vanish())
  }

  override def create(position: Position) = BombProp.create(position)

  override def getImage = BombProp.getImage
}

object BombProp extends ImageResource {
  override def getImageCachedPath = "images/prop_bomb.png"

  def create(position: Position) = {
    new BombProp(
      position, new AnimateContainer[Position](List(
        new AnimateLinear(position, new Position(position.getX, Main.WINDOW_HEIGHT),
          AnimateVectorType.PositionLike.id, getTimeMills, 20000)
      ))
    )
  }
}

