package edu.hitsz.aircraft

import edu.hitsz.application.{ImageResource, Main}

/**
 * 普通敌机，不可攻击
 *
 * @author chiro2001
 * @param locationXInit 初始 X 坐标
 * @param locationYInit 初始 Y 坐标
 * @param speedX        X 方向速度
 * @param speedY        Y 方向速度
 * @param hpInit        初始血量
 */
class MobEnemy(locationXInit: Int, locationYInit: Int, speedX: Int, speedY: Int, hpInit: Int)
  extends AbstractAircraft(locationXInit, locationYInit, speedX, speedY, hpInit) {
  override def forward() = {
    super.forward()
    // 判定 y 轴向下飞行出界
    if (locationY >= Main.WINDOW_HEIGHT) vanish()
  }

  override def shoot() = List()

  override def getImage = MobEnemy.getImage
}

object MobEnemy extends ImageResource {
  override def getImageCachedPath = "images/mob.png"
}