package edu.hitsz.application

import edu.hitsz.aircraft.HeroAircraft
import edu.hitsz.utils.SetInRangeInt

import java.awt.event.{MouseAdapter, MouseEvent}

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author chiro2001
 */
class HeroController(game: Game, heroAircraft: HeroAircraft) {
  private val mouseAdapter = new MouseAdapter() {
    override def mouseDragged(e: MouseEvent): Unit = {
      super.mouseDragged(e)
      // val x = e.getX
      // val y = e.getY
      val x = SetInRangeInt(e.getX, 0, Main.WINDOW_WIDTH)
      val y = SetInRangeInt(e.getY, 0, Main.WINDOW_HEIGHT - heroAircraft.getHeight / 2)
      // 防止超出边界
      // if (x < 0 || x > Main.WINDOW_WIDTH || y < 0 || y > Main.WINDOW_HEIGHT) return
      heroAircraft.setLocation(x, y)
    }
  }
  game.addMouseListener(mouseAdapter)
  game.addMouseMotionListener(mouseAdapter)
}