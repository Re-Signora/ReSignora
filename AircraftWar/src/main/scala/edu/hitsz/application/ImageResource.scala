package edu.hitsz.application

import edu.hitsz.utils.tryGetImageFile
import java.awt.image.BufferedImage

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author chiro2001
 */
trait ImageResource extends ImageResourceReady {
  def getImageCachedPath: String

  def getImage: BufferedImage = if (imageCache.isEmpty) {
    imageCache = Some(tryGetImageFile(getImageCachedPath))
    imageCache.get
  } else imageCache.get

  private var imageCache: Option[BufferedImage] = None
}

trait ImageResourceReady {
  def getImage: BufferedImage
}