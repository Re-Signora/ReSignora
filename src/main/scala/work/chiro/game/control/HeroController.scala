package work.chiro.game.control

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft.HeroAircraft
import work.chiro.game.application.Game
import work.chiro.game.basic.Vec2Double
import work.chiro.game.utils.{getTimeMills, setInRangeDouble, setInRangeInt}

import java.awt.event.{KeyAdapter, KeyEvent, MouseAdapter, MouseEvent}
import javax.swing.JFrame
import scala.collection.mutable

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author chiro2001
 */
class HeroController(frame: JFrame, game: Game) {
  // 使得窗口和 JPanel 能够获得键盘输入焦点
  frame.getContentPane.add(game)
  frame.setVisible(true)
  game.requestFocus()

  private val pressedKeys = mutable.Set[Int]()

  def pressed(keyCode: Int): Boolean = pressedKeys.contains(keyCode)

  private val mouseAdapter = new MouseAdapter {
    override def mouseDragged(e: MouseEvent): Unit = {
      super.mouseDragged(e)
      // 防止超出边界
      val x = setInRangeDouble(e.getX - config.window.playOffsetX, 0, config.window.playWidth)
      val y = setInRangeDouble(e.getY - config.window.playOffsetY, 0, config.window.playHeight - HeroAircraft.getInstance.getHeight / 2)
      HeroAircraft.getInstance.setLocation(x, y)
    }

    override def mousePressed(e: MouseEvent) = {
      super.mousePressed(e)
      game.requestFocus()
    }
  }
  game.addMouseListener(mouseAdapter)
  game.addMouseMotionListener(mouseAdapter)

  private val keyAdapter = new KeyAdapter {
    override def keyPressed(e: KeyEvent) = {
      pressedKeys.add(e.getKeyCode)
      // logger.info(s"pressed: ${e.getKeyChar} ${e.getKeyCode}")
    }

    override def keyReleased(e: KeyEvent) = pressedKeys.remove(e.getKeyCode)
  }
  game.addKeyListener(keyAdapter)

  var lastFrameTime = getTimeMills

  def onFrame() = {
    val frameTime = getTimeMills - lastFrameTime
    val next = Vec2Double()
    val scaleFast = 0.26
    val scaleSlow = 0.1
    val scale = if (pressedKeys.contains(config.control.keySlow)) scaleSlow else scaleFast

    pressedKeys.foreach {
      case config.control.keyUp => next.setY(-scale * config.control.moveSpeed * frameTime)
      case config.control.keyDown => next.setY(scale * config.control.moveSpeed * frameTime)
      case config.control.keyLeft => next.setX(-scale * config.control.moveSpeed * frameTime)
      case config.control.keyRight => next.setX(scale * config.control.moveSpeed * frameTime)
      case config.control.keyQuit => System.exit(0)
      case _ => ()
    }

    val newPos = HeroAircraft.getInstance.getPos + next
    HeroAircraft.getInstance.setLocation(
      setInRangeDouble(newPos.getX.toInt, 0, config.window.playWidth),
      setInRangeDouble(newPos.getY.toInt, 0, config.window.playHeight - HeroAircraft.getInstance.getHeight / 2)
    )
    lastFrameTime = getTimeMills
  }
}

object HeroController {
  var heroControllerInstance: Option[HeroController] = None

  def getInstance = heroControllerInstance.get

  def create(frame: JFrame, game: Game): HeroController = {
    if (heroControllerInstance.isEmpty) heroControllerInstance = Some(new HeroController(frame, game))
    heroControllerInstance.get
  }
}