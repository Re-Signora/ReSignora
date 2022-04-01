package work.chiro.game.application

import org.luaj.vm2.Globals
import org.luaj.vm2.lib.jse.JsePlatform
import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.{GlobalConfigLoader, logger}

import java.awt.Toolkit
import javax.swing.{JFrame, WindowConstants}
import java.awt.GraphicsEnvironment

/**
 * 程序入口
 *
 * @author chiro2001
 */
object Main {
  private var frameInstance: Option[JFrame] = None
  private var luaGlobals: Option[Globals] = None

  def getFrameInstance = frameInstance.get

  def getLuaGlobals = luaGlobals.get

  def main(args: Array[String]): Unit = {
    GlobalConfigLoader.init
    logger.info("Hello Aircraft War" + (if (config.isDebug) " [DEBUG MODE]" else ""))
    // 获得屏幕的分辨率，初始化 Frame
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    frameInstance = Some(new JFrame("Aircraft War"))
    val frame = frameInstance.get
    frame.setSize(config.window.width, config.window.height)
    frame.setResizable(false)
    // 设置窗口的大小和位置,居中放置
    frame.setBounds((screenSize.getWidth.toInt - config.window.width) / 2, 0, config.window.width, config.window.height)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    // 进入全屏模式
    if (config.window.fullScreen) {
      // 去除边框
      frame.setUndecorated(true)
      // 始终最前
      frame.setAlwaysOnTop(true)
      val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
      val gd = ge.getDefaultScreenDevice
      if (gd.isFullScreenSupported) gd.setFullScreenWindow(frame)
      else logger.warn("Unsupported full screen!")
    }
    luaGlobals = Some(JsePlatform.standardGlobals())
    getLuaGlobals.loadfile("scripts/lang-test.lua").call()
    new Game(frame).action()
  }
}
