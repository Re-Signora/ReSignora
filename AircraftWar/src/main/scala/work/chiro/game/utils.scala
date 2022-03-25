package work.chiro.game

import work.chiro.game.aircraft.MobEnemy
import work.chiro.game.application.Main
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
    (Math.random * (Main.WINDOW_WIDTH - imageWidth)).toInt * 1,
    (Math.random * Main.WINDOW_HEIGHT * 0.2).toInt * 1
  )
}
