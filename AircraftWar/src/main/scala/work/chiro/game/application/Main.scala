package work.chiro.game.application

import java.awt.Toolkit
import javax.swing.{JFrame, WindowConstants}
import work.chiro.game.GlobalConfigLoader.config

/**
 * 程序入口
 *
 * @author chiro2001
 */
object Main {
  private var frameInstance: Option[JFrame] = None

  def getFrameInstance = frameInstance

  def main(args: Array[String]): Unit = {
    System.out.println("Hello Aircraft War" + (if (config.isDebug) " [DEBUG MODE]" else ""))
    // 获得屏幕的分辨率，初始化 Frame
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    frameInstance = Some(new JFrame("Aircraft War"))
    val frame = frameInstance.get
    frame.setSize(config.window.width, config.window.height)
    frame.setResizable(false)
    //设置窗口的大小和位置,居中放置
    frame.setBounds((screenSize.getWidth.toInt - config.window.width) / 2, 0, config.window.width, config.window.height)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    // val game = new Game(frame)
    // frame.add(game)
    // 使用下面一行使得 JPanel 能够获得键盘焦点
    // frame.getContentPane.add(game)
    // frame.setVisible(true)
    // game.requestFocus()
    // game.action()
    new Game(frame).action()
  }
}
