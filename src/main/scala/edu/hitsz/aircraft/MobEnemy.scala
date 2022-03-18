package edu.hitsz.aircraft

import edu.hitsz.application.Main
import edu.hitsz.bullet.AbstractBullet

import java.util

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
class MobEnemy(locationX: Int, locationY: Int, speedX: Int, speedY: Int, hpInit: Int) extends AbstractAircraft(locationX, locationY, speedX, speedY, hpInit) {
  override def forward() = {
    super.forward()
    // 判定 y 轴向下飞行出界
    if (locationY >= Main.WINDOW_HEIGHT) vanish()
  }

  override def shoot() = List()
}