package work.chiro.game.basic

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions

abstract class VectorType[T](size: Int, dataInit: Seq[T] = Seq()) {
  def getDefaultValue: T

  val content = new ListBuffer[T]
  content.appendAll(for {_ <- 0 until size} yield getDefaultValue)
  set(dataInit)

  def getSize = size

  def get = content

  def set(values: Seq[T]): Unit = for (i <- 0 until size) content(i) =
    if (i < values.size) values(i) else getDefaultValue
}

abstract class Vec[T](size: Int, dataInit: Seq[T]) extends VectorType[T](size, dataInit) {
  val supportDifferentSize = false

  def calc[TRes <: Vec[T]](that: Vec[T])(implicit operate: (T, T) => T, getNewClass: (Int, Seq[T]) => TRes): TRes = {
    if (supportDifferentSize) {
      val newSize = math.max(size, that.getSize)
      getNewClass(newSize,
        for {i <- 0 until newSize} yield
          if (i < math.min(size, that.getSize))
            operate(content(i), that.get(i))
          else
            getDefaultValue
      )
    } else {
      assert(size == that.getSize, "Do not support calc between different size!")
      getNewClass(size, for {i <- 0 until size} yield operate(content(i), that.get(i)))
    }
  }

  override def toString = f"${get.mkString("Vec(", ", ", ")")}"
}

class VecInt(size: Int, dataInit: Seq[Int] = Seq()) extends Vec[Int](size, dataInit) {
  override def getDefaultValue = 0

  implicit val getNewClass = (sizeNew: Int, dataInitNew: Seq[Int]) => new VecInt(sizeNew, dataInitNew)

  def calc(that: VecInt, operator: String): VecInt = {
    implicit val operate = operator match {
      case "+" => (a: Int, b: Int) => a + b
      case "-" => (a: Int, b: Int) => a - b
      case "*" => (a: Int, b: Int) => a * b
      case "/" => (a: Int, b: Int) => a / b
    }
    this.calc[VecInt](that)
  }

  def +(that: VecInt) = calc(that, "+")

  def -(that: VecInt) = calc(that, "-")

  def *(that: VecInt) = calc(that, "*")

  def /(that: VecInt) = calc(that, "/")

  def copy: VecInt = new VecInt(size, content.toList)
}

class VecDouble(size: Int, dataInit: Seq[Double] = Seq()) extends Vec[Double](size, dataInit) {
  override def getDefaultValue = 0.0

  def set(that: VecDouble): Unit = set(that.get.toList)

  implicit val getNewClass = (sizeNew: Int, dataInitNew: Seq[Double]) => new VecDouble(sizeNew, dataInitNew)

  def calc(that: VecDouble, operator: String): VecDouble = {
    implicit val operate = operator match {
      case "+" => (a: Double, b: Double) => a + b
      case "-" => (a: Double, b: Double) => a - b
      case "*" => (a: Double, b: Double) => a * b
      case "/" => (a: Double, b: Double) => a / b
    }
    this.calc[VecDouble](that)
  }

  def +(that: VecDouble) = calc(that, "+")

  def -(that: VecDouble) = calc(that, "-")

  def *(that: VecDouble) = calc(that, "*")

  def /(that: VecDouble) = calc(that, "/")

  def +(that: Double) = calc(new VecDouble(size, Seq.fill(size)(that)), "+")

  def -(that: Double) = calc(new VecDouble(size, Seq.fill(size)(that)), "-")

  def *(that: Double) = calc(new VecDouble(size, Seq.fill(size)(that)), "*")

  def /(that: Double) = calc(new VecDouble(size, Seq.fill(size)(that)), "/")

  def copy: VecDouble = new VecDouble(size, content.toList)

  override def toString = f"${get.mkString("Vec(", ", ", ")")}"
}

class Vec1Double(xInit: Double = 0) extends VecDouble(1, Seq(xInit)) {
  def getX: Double = content.head

  def setX(value: Double) = content(0) = value
}

class Vec2Double(xInit: Double = 0, yInit: Double = 0) extends VecDouble(2, Seq(xInit, yInit)) {
  def getX: Double = content.head

  def getY: Double = content(1)

  def setX(value: Double) = content(0) = value

  def setY(value: Double) = content(1) = value

  override def toString = f"($getX%.3f, $getY%.3f)"

  def calc(that: Vec2Double, operator: String): Vec2Double =
    operator match {
      case "+" => new Vec2Double(getX + that.getX, getY + that.getY)
      case "-" => new Vec2Double(getX - that.getX, getY - that.getY)
      case "+=" =>
        setX(getX + that.getX)
        setY(getY + that.getY)
        this
      case "-=" =>
        setX(getX - that.getX)
        setY(getY - that.getY)
        this
    }

  def +(that: Vec2Double): Vec2Double = calc(that, "+")

  def -(that: Vec2Double): Vec2Double = calc(that, "-")

  def +=(that: Vec2Double): Vec2Double = calc(that, "+=")

  def -=(that: Vec2Double): Vec2Double = calc(that, "-=")

  override def copy: Vec2Double = new Vec2Double(getX, getY)
}

object Vec2Double {
  implicit def toVec2Double(vecDouble: VecDouble): Vec2Double = new Vec2Double(vecDouble.get.head, vecDouble.get(1))

  def apply(xInit: Double = 0, yInit: Double = 0) = new Vec2Double(xInit, yInit)
}

class Vec2Int(xInit: Int = 0, yInit: Int = 0) extends VecInt(2, Seq(xInit, yInit)) {
  def getX: Int = content.head

  def getY: Int = content(1)

  def setX(value: Int) = content(0) = value

  def setY(value: Int) = content(1) = value

  override def toString = f"($getX, $getY)"

  def calc(that: Vec2Int, operator: String): Vec2Int =
    operator match {
      case "+" => new Vec2Int(getX + that.getX, getY + that.getY)
      case "-" => new Vec2Int(getX - that.getX, getY - that.getY)
      case "+=" =>
        setX(getX + that.getX)
        setY(getY + that.getY)
        this
      case "-=" =>
        setX(getX - that.getX)
        setY(getY - that.getY)
        this
    }

  def +(that: Vec2Int): Vec2Int = calc(that, "+")

  def -(that: Vec2Int): Vec2Int = calc(that, "-")

  def +=(that: Vec2Int): Vec2Int = calc(that, "+=")

  def -=(that: Vec2Int): Vec2Int = calc(that, "-=")

  override def copy: Vec2Int = new Vec2Int(getX, getY)
}

object Vec2Int {
  implicit def toVec2Int(vecInt: VecInt): Vec2Int = new Vec2Int(vecInt.get.head, vecInt.get(1))

  def apply(xInit: Int = 0, yInit: Int = 0) = new Vec2Int(xInit, yInit)
}
