package work.chiro.game.basic

import work.chiro.game.aircraft.AbstractAircraft
import work.chiro.game.animate.AnimateContainer
import work.chiro.game.application.ImageResourceReady
import work.chiro.game.basic.PositionType.{Position, Scale, Size}
import work.chiro.game.utils.getTimeMills

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
                              sizeInit: Option[Size] = None,
                              rotationInit: Option[Scale] = None)
  extends ImageResourceReady {
  // println(s"Object created at $posInit ${getClass.getName}")
  protected var pos = posInit

  def getPos = pos

  def setPos(posNew: Position) = pos.set(posNew)

  def setPos(posX: Double, posY: Double) = pos.set(new Position(posX, posY))

  // 尺寸 -1 表示未设置，等待加载图片后依据图片大小自动设置
  val size = if (sizeInit.nonEmpty) sizeInit.get else new Size(-1, -1)

  // 旋转角度（弧度制）
  val rotation = if (rotationInit.nonEmpty) rotationInit.get else {
    val vec = animateContainer.getDelta
    if (vec.getSize == 0) new Scale
    else {
      assert(vec.getSize == 2)
      // new Scale(math.atan(vec.get(1) / (if (vec.get.head != 0.0) vec.get.head else 1e-5)))
      val r = -math.atan(vec.get.head / (if (vec.get(1) != 0.0) vec.get(1) else 1e-5))
      if (vec.get(1) > -1)
      // println(s"${getClass.getName} r = $r, vec = $vec")
      new Scale(r)
    }
  }

  /**
   * 有效（生存）标记，
   * 通常标记为 false的对象会再下次刷新时清除
   */
  protected var valid = true

  /**
   * 可飞行对象根据速度移动
   * 若飞行对象触碰到横向边界，横向速度反向
   */
  def forward(): Unit = {
    animateContainer.updateAll(getTimeMills)
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
    val fWidth = theObject.getWidth
    val fHeight = theObject.getHeight
    x + (fWidth + getWidth) / 2 > getLocationX &&
      x - (fWidth + getWidth) / 2 < getLocationX &&
      y + (fHeight / fFactor + getHeight / factor) / 2 > getLocationY &&
      y - (fHeight / fFactor + getHeight / factor) / 2 < getLocationY
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

  def isValid = this.valid

  /**
   * 标记消失，
   * isValid = false.
   * notValid() => true.
   */
  // def vanish() = valid = false
  def vanish() = {
    // println(s"${getClass.getName} vanish!")
    valid = false
  }
}

trait AbstractObjectFactory {
  def create(): AbstractObject
}