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
      // 防止超出边界
      val x = setInRangeInt(e.getX, 0, config.window.width)
      val y = setInRangeInt(e.getY, 0, config.window.height - heroAircraft.getHeight / 2)
      heroAircraft.setLocation(x, y)
    }
  }
  game.addMouseListener(mouseAdapter)
  game.addMouseMotionListener(mouseAdapter)

  private val keyAdapter = new KeyAdapter {
    override def keyPressed(e: KeyEvent) = {
      pressedKeys.add(e.getKeyCode)
      // println(s"pressed: ${e.getKeyChar} ${e.getKeyCode}")
    }

    override def keyReleased(e: KeyEvent) = pressedKeys.remove(e.getKeyCode)
  }
  game.addKeyListener(keyAdapter)

  def onFrame() = {
    val next = Vec2Double()
    val scaleFast = 8.0
    val scaleSlow = 2.0
    val scale = if (pressedKeys.contains(config.control.keySlow)) scaleSlow else scaleFast

    pressedKeys.foreach {
      case config.control.keyUp => next.setY(-scale * config.control.moveSpeed)
      case config.control.keyDown => next.setY(scale * config.control.moveSpeed)
      case config.control.keyLeft => next.setX(-scale * config.control.moveSpeed)
      case config.control.keyRight => next.setX(scale * config.control.moveSpeed)
      case config.control.keyQuit => System.exit(0)
      case _ => ()
    }

    val newPos = heroAircraft.getPos + next
    heroAircraft.setLocation(
      setInRangeInt(newPos.getX.toInt, 0, config.window.width),
      setInRangeInt(newPos.getY.toInt, 0, config.window.height - heroAircraft.getHeight / 2)
    )
  }
}