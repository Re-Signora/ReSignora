package edu.hitsz.animate

import edu.hitsz.basic._

class AnimateTypeEnumeration extends Enumeration

object AnimateType extends AnimateTypeEnumeration {
  val Unknown, Linear, Nonlinear = Value
}

class AnimateVectorTypeEnumeration extends Enumeration

object AnimateVectorType extends AnimateVectorTypeEnumeration {
  val PositionLike, Others = Value
}

abstract class AbstractAnimate[V <: VecDouble]
(vecSource: V, vecTarget: V, animateType: Int, animateVectorType: Int, timeStart: Double, timeSpan: Double) {
  def getAnimateType: Int = animateType

  def getAnimateVectorType: Int = animateVectorType

  def getVector: V = vec

  val vec = vecSource
  val source = vecSource.copy
  val target = vecTarget
  val delta = target - source
  // println(s"typeOf T = ${animateType.getClass.getName}")

  def update(timeNow: Double): Boolean

  def isDone(timeNow: Double) = timeNow > timeStart + timeSpan

  def getSpeed(timeNow: Double): VecDouble
}

class AnimateLinear[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Linear.id, animateVectorType, timeStart, timeSpan) {

  override def update(timeNow: Double) = {
    val done = isDone(timeNow)
    if (done) vec.set(source + (delta * ((timeNow - timeStart) / timeSpan)))
    else vec.set(vecTarget)
    done
  }

  override def getSpeed(timeNow: Double) =
    if (animateVectorType == AnimateVectorType.PositionLike.id) delta / timeSpan
    else new VecDouble(getVector.getSize)
}

class AnimateNonLinear[V <: VecDouble, EV <: AnimateVectorTypeEnumeration#Value]
(vecSource: V, vecTarget: V, animateVectorType: EV, timeStart: Double, timeSpan: Double, speedInit: V, a: V)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Nonlinear.id, animateVectorType.id, timeStart, timeSpan) {

  override def update(timeNow: Double) = {
    // x = x_0 + v_0 * t + 1/2 * a * t^2
    val t = timeNow - timeStart
    val done = isDone(timeNow)
    if (done) vec.set(vecTarget)
    else vec.set(source + speedInit * t + a * t * t / 2)
    done
  }

  override def getSpeed(timeNow: Double) =
    if (animateVectorType == AnimateVectorType.PositionLike) speedInit + a * (timeNow - timeStart)
    else new VecDouble(getVector.getSize)
}

class AnimateContainer[V <: VecDouble]
(animateSeq: List[AbstractAnimate[V]] = List()) {
  def getAnimateSeq = animateSeq

  def updateAll(timeNow: Double) = animateSeq.map(a => a.update(timeNow))

  def getSpeed(timeNow: Double): VecDouble = {
    val positionLikeAnimates = animateSeq.filter(_.getAnimateVectorType == AnimateVectorType.PositionLike.id)
      positionLikeAnimates.map(_.getSpeed(timeNow)).reduce(_ + _)
  }
}

// object AnimateTestType extends App {
//   val animate = new AbstractAnimate(new Vec2Double, new Vec2Double, AnimateType.Linear)
// }