package work.chiro.game.bullet

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.application.ImageResource
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.Vec2Double

/**
 * @author chiro2001
 */
class EnemyBullet(posInit: Position, animateContainer: AnimateContainer[Vec2Double], power: Int)
  extends AbstractBullet(posInit, animateContainer, power)
  with ImageResource {
  override def getImageCachedPath = "images/bullet_enemy.png"
}