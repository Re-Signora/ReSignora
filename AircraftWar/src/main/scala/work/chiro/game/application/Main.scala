package work.chiro.game.application

import java.awt.Toolkit
import javax.swing.{JFrame, WindowConstants}

/**
 * 程序入口
 *
 * @author chiro2001
 */
object Main {
  val WINDOW_WIDTH = 512
  val WINDOW_HEIGHT = 768

  def main(args: Array[String]): Unit = {
    System.out.println("Hello Aircraft War")
    // 获得屏幕的分辨率，初始化 Frame
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val frame = new JFrame("Aircraft War")
    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT)
    frame.setResizable(false)
    //设置窗口的大小和位置,居中放置
    frame.setBounds((screenSize.getWidth.toInt - WINDOW_WIDTH) / 2, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    val game = new Game
    frame.add(game)
    frame.setVisible(true)
    game.action()
  }
}
