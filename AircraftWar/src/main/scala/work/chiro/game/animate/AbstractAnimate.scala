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

  def isDone(timeNow: Double) = {
    val done = timeNow > timeStart + timeSpan
    // if (done) println(f"[$timeNow%.3f] ${getClass.getName} done!!")
    done
  }

  def getSpeed(timeNow: Double): VecDouble
}

class AnimateLinearToTarget[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, willStop: Boolean = true)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Linear.id, animateVectorType, timeStart, timeSpan) {

  // println(s"Animate Linear $vecSource => $vecTarget")
  override def isDone(timeNow: Double) = if (willStop) super.isDone(timeNow) else false

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

class AnimateNonLinearToTargetVec[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, speedMax: Double, a: Double, softStop: Boolean = true, willStop: Boolean = true)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Nonlinear.id, animateVectorType, timeStart, timeSpan) {

  override def isDone(timeNow: Double) = if (willStop) super.isDone(timeNow) else false

  // val timeNonLinear = VecDouble.min(speedMax / a, ((getDelta * (if (softStop) 1 else 2)) / a).sqrt)
  val timeNonLinear = if (a == 0) 0 else speedMax / a
  // fixme: 如果达不到最高速度，timeLinear 应该为 0
  val timeLinear = timeNonLinear * -(if (softStop) 1 else 2) + timeSpan
  val unit = getDelta / getDelta.scale

  override def update(timeNow: Double) = {
    // // x = x_0 + 1/2 * a * t^2
    // val t = timeNow - timeStart
    // val done = isDone(timeNow)
    // // val done = true
    // if (done) println(f"[$timeNow%.3f] ${getClass.getName} done!!")
    // if (done) getVector.set(vecTarget)
    // else getVector.set(getSource + (a * t * t / 2) * getDelta * t / timeSpan)
    // done
    val t = timeNow - timeStart
    val done = isDone(timeNow)
    if (done) println(f"[$timeNow%.3f] ${getClass.getName} done!!")
    if (done) getVector.set(vecTarget)
    else {
      if (t < timeNonLinear) getVector.set(getSource + unit * a * (t * t / 2))
      else if (t < timeNonLinear + timeLinear) {
        getVector.set(getSource + unit * a * (timeNonLinear * timeNonLinear / 2) +
          unit * speedMax * (t - timeNonLinear))
      } else {
        if (softStop) {
          getVector.set(getSource + unit * a * (timeNonLinear * timeNonLinear / 2) +
            unit * speedMax * timeNonLinear +
            unit * a * (timeNonLinear * timeNonLinear / 2))
        }
      }
    }
    done
  }

  override def getSpeed(timeNow: Double) = {
    val t = timeNow - timeStart
    if (animateVectorType == AnimateVectorType.PositionLike.id && !isDone(timeNow))
      unit * (
        if (t < timeNonLinear) t * a
        else if (t < timeNonLinear + timeLinear) speedMax
        else if (softStop) (timeNonLinear * 2 + timeLinear - t) * a
        else speedMax
        )
    else new VecDouble(getVector.getSize)
  }
}

// class AnimateNonLinearToTargetScale[V <: VecDouble]
// (vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, speedMax: Double, a: Double)
//   extends AnimateNonLinearToTargetVec(vecSource, vecTarget, animateVectorType, timeStart, timeSpan, VecDouble.toDirection(vecSource, vecTarget, speedMax), VecDouble.toDirection(vecSource, vecTarget, a))