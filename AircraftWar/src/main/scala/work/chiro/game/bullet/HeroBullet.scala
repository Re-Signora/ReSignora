package work.chiro.game.bullet

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double

/**
 * @author chiro2001
 */
class HeroBullet(posInit: Position, animateContainer: AnimateContainer[Vec2Double], power: Int)
  extends AbstractBullet(posInit, animateContainer, power)
  with ImageResourceFactory {
  override def getImageCachedPath = "images/bullet_hero.png"
}