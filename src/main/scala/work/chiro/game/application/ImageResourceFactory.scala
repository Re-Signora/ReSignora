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
  // 缓存上次加载的路径，变化的时候再重新加载
  private var imagePathCached: Option[String] = None
  def getImageCachedPath: String

  def getImage: BufferedImage = if (imageCached.isEmpty || imagePathCached.isEmpty) {
    imageCached.synchronized({
      imageCached = Some(tryGetImageFile(getImageCachedPath))
    })
    imagePathCached = Some(new String(getImageCachedPath))
    imageCached.get
  } else {
    if (imagePathCached.get == getImageCachedPath) {
      imageCached.get
    } else {
      imagePathCached = None
      getImage
    }
  }
}

trait ImageResourceReady {
  def getImage: BufferedImage
  def hasImage = imageCached.isDefined
  var imageCached: Option[BufferedImage] = None
}