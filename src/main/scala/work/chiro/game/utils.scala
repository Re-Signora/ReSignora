package work.chiro.game

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.basic.PositionType.Position

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object utils {
  def tryGetImageFile(filename: String): BufferedImage = {
    def getImage(path: String) = ImageIO.read(getClass.getClassLoader.getResource(path).openStream())

    getImage(f"$filename")
  }

  val timeStartGlobal: Long = System.currentTimeMillis

  def getTimeMills: Double = (System.currentTimeMillis - timeStartGlobal).toDouble

  def setInRangeInt(source: Int, downTo: Int, upTo: Int) = if (source < downTo) downTo else if (source > upTo) upTo else source

  def getNewFlightPosition(imageWidth: Int) = new Position(
    (math.random() * (config.window.width - imageWidth)).toInt * 1,
    (math.random() * config.window.height * 0.2).toInt * 1
  )
}

object logger {
  val LOG_LEVER_DEBUG = 0
  val LOG_LEVER_VERBOSE = 1
  val LOG_LEVER_INFO = 2
  val LOG_LEVER_WARN = 3
  val LOG_LEVER_ERROR = 4
  val LOG_LEVER_FATAL = 5
  var logLevel = 0

  protected def logIt(foo: String, level: Int)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name) = {
    if (level >= logLevel)
      println(s"${file.value}:${line.value} [${name.value}] $foo")
  }

  def log(msg: => String)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit = logIt(msg, LOG_LEVER_VERBOSE)

  def debug(msg: => String)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit = logIt(f"DEBUG - $msg", LOG_LEVER_DEBUG)

  def info(msg: => String)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit = logIt(f"INFO - $msg", LOG_LEVER_INFO)

  def warn(msg: => String)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit = logIt(f"WARN - $msg", LOG_LEVER_WARN)

  def error(msg: => String)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit = logIt(f"ERROR - $msg", LOG_LEVER_ERROR)

  def fatal(msg: => String)(implicit line: sourcecode.Line, file: sourcecode.File, name: sourcecode.Name): Unit = logIt(f"FATAL - $msg", LOG_LEVER_FATAL)
}
