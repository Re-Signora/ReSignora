package edu.hitsz.bullet

import edu.hitsz.application.ImageResource

/**
 * @author chiro2001
 */
class HeroBullet(locationXInit: Int, locationYInit: Int, speedX: Int, speedY: Int, power: Int)
  extends AbstractBullet(locationXInit, locationYInit, speedX, speedY, power)
  with ImageResource {
  override def getImageCachedPath = "images/bullet_hero.png"
}