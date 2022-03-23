package edu.hitsz.basic

import scala.collection.mutable.ListBuffer

abstract class VectorType[T](size: Int) {
  def getDefaultValue: T
  // val content = Seq.fill[T](size)(getDefaultValue).toList
  val content = ListBuffer[T](size)
}

abstract class Vec[T](size: Int, argsInit: Seq[T]) extends VectorType[T](size) {
  set(argsInit)

  def get = content

  def set(values: T*) = set(values)

  def set(values: Seq[T]) = for (i <- 0 until size) content(i) = if (i < values.size) values(i) else 0

  def calc[TRes](that: Vec[T], operate: (T, T) => T): TRes = {
    val newSize = math.max(size, that.size)
    new TRes(newSize,
      for {i <- 0 until newSize} yield
        if (i < math.min(size, that.size))
          operate(content(i), that.get(i))
        else
          getDefaultValue
    )
  }

  def +[TRes <: Vec[T]](that: TRes): TRes = calc(that, _ + _)
}

class VecInt(size: Int, argsInit: Seq[Int]) extends Vec[Int](size, argsInit) {
  override def getDefaultValue = 0
}

class VecDouble(size: Int, argsInit: Seq[Double]) extends Vec[Double](size, argsInit) {
  override def getDefaultValue = 0.0
}

class Vec1Double(xInit: Double) extends VecDouble(1, Seq(xInit)) {
  def getX: Double = content(0)

  def setX(value: Double) = content(0) = value
}

class Vec2Double(xInit: Double, yInit: Double) extends VecDouble(2, Seq(xInit, yInit)) {
  def getX: Double = content(0)

  def getY: Double = content(1)

  def setX(value: Double) = content(0) = value

  def setY(value: Double) = content(1) = value
}

