package edu.hitsz

import java.awt.image.BufferedImage
import java.io.{FileInputStream, FileNotFoundException}
import javax.imageio.ImageIO

object utils {
  def tryGetImageFile(filename: String): BufferedImage = {
    def getImage(path: String) = ImageIO.read(getClass.getClassLoader.getResource(path).openStream())

    getImage(f"$filename")
  }

}
