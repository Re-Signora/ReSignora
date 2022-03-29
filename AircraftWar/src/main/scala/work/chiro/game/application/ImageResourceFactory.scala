package work.chiro.game.application

import work.chiro.game.utils.tryGetImageFile
import java.awt.image.BufferedImage

/**
 * 管理图片的加载，访问；提供图片的静态访问方法<br/>
 * 使用工厂方式构建，同时对每个资源实现单例模式
 *
 * @author chiro2001
 */
trait ImageResourceFactory extends ImageResourceReady {
  def getImageCachedPath: String

  def getImage: BufferedImage = if (imageCache.isEmpty) {
    imageCache.synchronized({
      imageCache = Some(tryGetImageFile(getImageCachedPath))
    })
    imageCache.get
  } else imageCache.get
}

trait ImageResourceReady {
  def getImage: BufferedImage
  def hasImage = imageCache.isDefined
  var imageCache: Option[BufferedImage] = None
}