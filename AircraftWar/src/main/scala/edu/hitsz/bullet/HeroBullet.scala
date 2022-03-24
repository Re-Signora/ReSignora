package edu.hitsz.bullet

import edu.hitsz.animate.AnimateContainer
import edu.hitsz.application.ImageResource
import edu.hitsz.basic.PositionType.Position
import edu.hitsz.basic.Vec2Double

/**
 * @author chiro2001
 */
class HeroBullet(posInit: Position, animateContainer: AnimateContainer[Vec2Double], power: Int)
  extends AbstractBullet(posInit, animateContainer, power)
  with ImageResource {
  override def getImageCachedPath = "images/bullet_hero.png"
}