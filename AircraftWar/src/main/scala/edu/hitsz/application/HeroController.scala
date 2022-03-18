package edu.hitsz.application

import edu.hitsz.aircraft.HeroAircraft

import java.awt.event.{MouseAdapter, MouseEvent}

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author chiro2001
 */
class HeroController(var game: Game, var heroAircraft: HeroAircraft) {
  private val mouseAdapter = new MouseAdapter() {
    override def mouseDragged(e: MouseEvent): Unit = {
      super.mouseDragged(e)
      val x = e.getX
      val y = e.getY
      if (x < 0 || x > Main.WINDOW_WIDTH || y < 0 || y > Main.WINDOW_HEIGHT) { // 防止超出边界
        return
      }
      heroAircraft.setLocation(x, y)
    }
  }
  game.addMouseListener(mouseAdapter)
  game.addMouseMotionListener(mouseAdapter)
}