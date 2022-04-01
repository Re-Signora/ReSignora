package work.chiro.game.bullet

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.ScaleType.Position
import work.chiro.game.basic.Vec2Double

/**
 * @author chiro2001
 */
class EnemyBullet(posInit: Position, animateContainer: AnimateContainer, power: Int)
  extends AbstractBullet(posInit, animateContainer, power)
  with ImageResourceFactory {
  override def getImageCachedPath = "images/bullet_enemy.png"
}