package work.chiro.game.bullet

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.ScaleType.Position

/**
 * @author chiro2001
 */
class EnemyBullet(posInit: Position, animateContainer: AnimateContainer, power: Int)
  extends AbstractBullet(posInit, animateContainer, power) {
  override def getImage = EnemyBullet.getImage
}

object EnemyBullet extends ImageResourceFactory {
  override def getImageCachedPath = "images/bullet_enemy.png"
}