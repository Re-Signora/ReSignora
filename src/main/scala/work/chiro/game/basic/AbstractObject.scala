package work.chiro.game.basic

import work.chiro.game.aircraft.AbstractAircraft
import work.chiro.game.animate.AnimateContainer
import work.chiro.game.application.ImageResourceReady
import work.chiro.game.basic.PositionType.{Position, Scale, Size, SizeDouble}
import work.chiro.game.utils.getTimeMills
import work.chiro.game.GlobalConfigLoader.config

import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.{Graphics, Graphics2D}

/**
 * 所有对象的基类
 *
 * @author chiro2001
 * @param posInit          初始位置
 * @param animateContainer 动画容器
 * @param sizeInit         初始化大小，不设置则使用图片大小
 */
abstract class AbstractObject(posInit: Position,
                              animateContainer: AnimateContainer[Vec2Double],
                              sizeInit: Option[SizeDouble] = None,
                              rotationInit: Option[Scale] = None)
  extends ImageResourceReady {
  protected var pos = posInit

  def getPos = pos

  def setPos(posNew: Position) = pos.set(posNew)

  def setPos(posX: Double, posY: Double) = pos.set(new Position(posX, posY))

  // 尺寸 -1 表示未设置，等待加载图片后依据图片大小自动设置
  private val size = if (sizeInit.nonEmpty) sizeInit.get else new SizeDouble(-1, -1)

  def getSize = {
    getWidth
    getHeight
    size
  }

  def updateRotation(rotationNew: Option[Scale] = None) =
    if (rotationNew.nonEmpty) rotationNew.get else animateContainer.getRotation

  // 旋转角度（弧度制）
  private var rotation = updateRotation(rotationInit)

  def getRotation(update: Boolean = false): Scale = {
    if (update) {
      rotation = updateRotation()
      rotation
    } else {
      rotation
    }
  }

  /**
   * 有效（生存）标记，
   * 通常标记为 false的对象会再下次刷新时清除
   */
  protected var valid = true

  def forward(): Unit = {
    if (animateContainer.updateAll(getTimeMills)) vanish()
  }

  /**
   * 碰撞检测，当对方坐标进入我方范围，判定我方击中<br>
   * 对方与我方覆盖区域有交叉即判定撞击。
   * <br>
   * 非飞机对象区域：
   * 横向，[x - width/2, x + width/2]
   * 纵向，[y - height/2, y + height/2]
   * <br>
   * 飞机对象区域：
   * 横向，[x - width/2, x + width/2]
   * 纵向，[y - height/4, y + height/4]
   *
   * @param theObject 撞击对方
   * @return true: 我方被击中; false 我方未被击中
   */
  def crash(theObject: AbstractObject) = {
    // 缩放因子，用于控制 y轴方向区域范围
    val factor = if (this.isInstanceOf[AbstractAircraft]) 2 else 1
    val fFactor = if (theObject.isInstanceOf[AbstractAircraft]) 2 else 1
    val x = theObject.getLocationX
    val y = theObject.getLocationY
    val boxUse = theObject.getBoxSize
    // val fWidth = if (useImageBox) theObject.getImageWidth else theObject.getWidth
    // val fHeight = if (useImageBox) theObject.getImageHeight else theObject.getHeight
    x + (boxUse.getX + getWidth) / 2 > getLocationX &&
      x - (boxUse.getX + getWidth) / 2 < getLocationX &&
      y + (boxUse.getY / fFactor + getHeight / factor) / 2 > getLocationY &&
      y - (boxUse.getY / fFactor + getHeight / factor) / 2 < getLocationY
  }

  def getLocationX = getPos.getX

  def getLocationY = getPos.getY

  def setLocation(locationX: Double, locationY: Double) = setPos(locationX, locationY)

  def getSpeedY = animateContainer.getSpeed(getTimeMills).get(1)

  def getWidth = {
    // 若未设置，则查询图片宽度并设置
    if (size.getX == -1) size.setX(getImage.getWidth)
    size.getX
  }

  def getHeight = {
    // 若未设置，则查询图片高度并设置
    if (size.getY == -1) size.setY(getImage.getHeight)
    size.getY
  }

  def getImageWidth = getImage.getWidth

  def getImageHeight = getImage.getHeight

  def isValid = this.valid

  /**
   * 标记消失，
   * isValid = false.
   * notValid() => true.
   */
  def vanish() = valid = false

  def draw(g: Graphics,
           img: Option[BufferedImage] = None,
           position: Option[Position] = None,
           sizeDraw: Option[SizeDouble] = None,
           alignCenter: Boolean = true
          ): Unit = {
    val image = if (img.isEmpty) getImage else img.get
    val pos = if (position.isEmpty) getPos else position.get
    val sizeUse = if (sizeDraw.nonEmpty) sizeDraw.get else getSize
    if (getRotation().getX == 0) {
      g.drawImage(
        image,
        (pos.getX - (if (alignCenter) sizeUse.getX / 2 else 0)).toInt + config.window.playOffsetX,
        (pos.getY - (if (alignCenter) sizeUse.getY / 2 else 0)).toInt + config.window.playOffsetY,
        sizeUse.getX.toInt,
        sizeUse.getY.toInt,
        null)
    } else {
      val af = AffineTransform.getTranslateInstance(
        pos.getX - (if (alignCenter) sizeUse.getX / 2 else 0) + config.window.playOffsetX,
        pos.getY - (if (alignCenter) sizeUse.getY / 2 else 0) + config.window.playOffsetY)
      af.rotate(rotation.getX, sizeUse.getX / 2, sizeUse.getY / 2)
      val graphics2d = g.asInstanceOf[Graphics2D]
      graphics2d.drawImage(image, af, null)
    }
  }

  def draw(g: Graphics): Unit = draw(g, img = None, position = None, sizeDraw = None)

  def getBoxSize = getSize
}

trait AbstractObjectFactory {
  def create(): AbstractObject
}