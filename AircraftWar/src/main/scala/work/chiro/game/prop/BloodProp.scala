package work.chiro.game.prop

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft.{AbstractAircraft, HeroAircraft, MobEnemy}
import work.chiro.game.animate.{AnimateContainer, AnimateLinearToTarget, AnimateVectorType}
import work.chiro.game.application.{ImageResource, Main}
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double
import work.chiro.game.utils.{getNewFlightPosition, getTimeMills}

class BloodProp(posInit: Position, animateContainer: AnimateContainer[Vec2Double])
  extends AbstractProp(posInit, animateContainer) {
  val bloodIncrease = 100

  override def handleAircrafts(enemyAircrafts: List[AbstractAircraft]) =
    HeroAircraft.getHeroInstance.increaseHp(bloodIncrease)

  override def create(position: Position) = BloodProp.create(position)

  override def getImage = BloodProp.getImage
}

object BloodProp extends ImageResource {
  override def getImageCachedPath = "images/prop_blood.png"

  def create(position: Position) = {
    new BloodProp(
      position, new AnimateContainer[Position](List(
        new AnimateLinearToTarget(position, new Position(position.getX, config.window.height),
          AnimateVectorType.PositionLike.id, getTimeMills, 20000)
      ))
    )
  }
}
