package work.chiro.game.animate

import work.chiro.game.basic.PositionType.Scale
import work.chiro.game.basic.VecDouble

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

  private val vec = vecSource
  private val source = vecSource.copy
  private val target = vecTarget
  private val delta = target - source

  def getSource = source

  def getTarget = target

  def getDelta = delta

  def update(timeNow: Double): Boolean

  def isDone(timeNow: Double) = timeNow > timeStart + timeSpan

  def getSpeed(timeNow: Double): VecDouble
}

class AnimateLinearToTarget[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Linear.id, animateVectorType, timeStart, timeSpan) {

  // println(s"Animate Linear $vecSource => $vecTarget")

  override def update(timeNow: Double) = {
    val done = isDone(timeNow)
    val deltaNew = getDelta * ((timeNow - timeStart) / timeSpan)
    // println(f"delta = $delta deltaNew = $deltaNew")
    if (done) getVector.set(vecTarget)
    else getVector.set(getSource + deltaNew)
    done
  }

  override def getSpeed(timeNow: Double) =
    if (animateVectorType == AnimateVectorType.PositionLike.id) getDelta / timeSpan
    else new VecDouble(getVector.getSize)
}

class AnimateNonLinearToTarget[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, speedInit: V, a: V)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Nonlinear.id, animateVectorType, timeStart, timeSpan) {

  override def update(timeNow: Double) = {
    // x = x_0 + v_0 * t + 1/2 * a * t^2
    val t = timeNow - timeStart
    val done = isDone(timeNow)
    if (done) getVector.set(vecTarget)
    else getVector.set(getSource + (speedInit * t + a * t * t / 2) * getDelta / timeSpan)
    done
  }

  override def getSpeed(timeNow: Double) =
    if (animateVectorType == AnimateVectorType.PositionLike.id) speedInit + a * (timeNow - timeStart)
    else new VecDouble(getVector.getSize)
}

class AnimateContainer[V <: VecDouble]
(animateSeq: List[AbstractAnimate[V]] = List()) {
  def getAnimateSeq = animateSeq

  def updateAll(timeNow: Double) = animateSeq.map(_.update(timeNow))

  def getSpeed(timeNow: Double): VecDouble = {
    if (animateSeq.isEmpty) new VecDouble(0)
    else {
      val positionLikeAnimates = animateSeq.filter(_.getAnimateVectorType == AnimateVectorType.PositionLike.id)
      positionLikeAnimates.map(_.getSpeed(timeNow)).reduce(_ + _)
    }
  }

  def getDelta: VecDouble = {
    if (animateSeq.isEmpty) new VecDouble(0)
    else {
      val positionLikeAnimates = animateSeq.filter(_.getAnimateVectorType == AnimateVectorType.PositionLike.id)
      positionLikeAnimates.map(_.getDelta).reduce(_ + _)
    }
  }

  def getRotation: Scale = {
    if (getDelta.getSize == 0) new Scale
    else {
      assert(getDelta.getSize == 2)
      val r = -math.atan(getDelta.get.head / (if (getDelta.get(1) != 0.0) getDelta.get(1) else 1e-5))
      new Scale(r)
    }
  }
}

// object AnimateTestType extends App {
//   val animate = new AbstractAnimate(new Vec2Double, new Vec2Double, AnimateType.Linear)
// }