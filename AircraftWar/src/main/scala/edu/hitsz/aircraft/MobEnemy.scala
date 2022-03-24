package edu.hitsz.aircraft

import edu.hitsz.animate.AnimateContainer
import edu.hitsz.application.{ImageResource, Main}
import edu.hitsz.basic.PositionType.Position
import edu.hitsz.basic.Vec2Double

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
class MobEnemy(posInit: Position, animateContainer: AnimateContainer[Vec2Double], hpInit: Int)
  extends AbstractAircraft(posInit, animateContainer, hpInit) {
  override def forward() = {
    super.forward()
    // 判定 y 轴向下飞行出界
    if (getLocationY >= Main.WINDOW_HEIGHT) vanish()
  }

  override def shoot() = List()

  override def getImage = MobEnemy.getImage
}

object MobEnemy extends ImageResource {
  override def getImageCachedPath = "images/mob.png"
}