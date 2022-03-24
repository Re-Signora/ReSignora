package edu.hitsz

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object utils {
  def tryGetImageFile(filename: String): BufferedImage = {
    def getImage(path: String) = ImageIO.read(getClass.getClassLoader.getResource(path).openStream())

    getImage(f"$filename")
  }

  val timeStartGlobal: Long = System.currentTimeMillis

  def getTimeMills: Double = (System.currentTimeMillis - timeStartGlobal).toDouble

  def SetInRangeInt(source: Int, downTo: Int, upTo: Int) = if (source < downTo) downTo else if (source > upTo) upTo else source
}
