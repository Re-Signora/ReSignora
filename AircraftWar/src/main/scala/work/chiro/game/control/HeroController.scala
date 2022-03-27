package work.chiro.game.control

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft.HeroAircraft
import work.chiro.game.application.Game
import work.chiro.game.basic.Vec2Double
import work.chiro.game.utils.setInRangeInt

import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import scala.collection.mutable

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author chiro2001
 */
class HeroController(game: Game, heroAircraft: HeroAircraft) {
  private val pressedKeys = mutable.Set[Int]()
  private val mouseAdapter = new MouseAdapter {
    override def mouseDragged(e: MouseEvent): Unit = {
      super.mouseDragged(e)
      // val x = e.getX
      // val y = e.getY
      val x = setInRangeInt(e.getX, 0, config.window.width)
      val y = setInRangeInt(e.getY, 0, config.window.height - heroAircraft.getHeight / 2)
      // 防止超出边界
      // if (x < 0 || x > Main.WINDOW_WIDTH || y < 0 || y > Main.WINDOW_HEIGHT) return
      heroAircraft.setLocation(x, y)
    }
  }
  game.addMouseListener(mouseAdapter)
  game.addMouseMotionListener(mouseAdapter)

  private val keyAdapter = new KeyAdapter {
    override def keyPressed(e: KeyEvent) = pressedKeys.add(e.getKeyCode)

    override def keyReleased(e: KeyEvent) = pressedKeys.remove(e.getKeyCode)
  }
  game.addKeyListener(keyAdapter)

  def onFrame() = {
    val next = Vec2Double()
    val scale = 8.0

    pressedKeys.foreach {
      case config.control.keyUp => next.setY(-scale * config.control.moveSpeed)
      case config.control.keyDown => next.setY(scale * config.control.moveSpeed)
      case config.control.keyLeft => next.setX(-scale * config.control.moveSpeed)
      case config.control.keyRight => next.setX(scale * config.control.moveSpeed)
    }

    val newPos = heroAircraft.getPos + next
    heroAircraft.setLocation(
      setInRangeInt(newPos.getX.toInt, 0, config.window.width),
      setInRangeInt(newPos.getY.toInt, 0, config.window.height - heroAircraft.getHeight / 2)
    )
  }
}