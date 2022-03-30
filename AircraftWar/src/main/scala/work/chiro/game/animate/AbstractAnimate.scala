package work.chiro.game.animate

import work.chiro.game.basic.VecDouble

class AnimateTypeEnumeration extends Enumeration

object AnimateType extends AnimateTypeEnumeration {
  val Unknown, Linear, NonLinear, Smooth = Value
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
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, willStop: Boolean = true)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Linear.id, animateVectorType, timeStart, timeSpan) {

  override def isDone(timeNow: Double) = if (willStop) super.isDone(timeNow) else false

  override def update(timeNow: Double) = {
    val done = isDone(timeNow)
    val deltaNew = getDelta * ((timeNow - timeStart) / timeSpan)
    if (done) getVector.set(vecTarget)
    else getVector.set(getSource + deltaNew)
    done
  }

  override def getSpeed(timeNow: Double) =
    if (animateVectorType == AnimateVectorType.PositionLike.id) getDelta / timeSpan
    else new VecDouble(getVector.getSize)
}

class AnimateNonLinear[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.NonLinear.id, animateVectorType, timeStart, timeSpan) {

  override def update(timeNow: Double) = {
    val t = timeNow - timeStart
    val done = isDone(timeNow)
    if (done) getVector.set(vecTarget)
    else getVector.set(getSource + getDelta * (t * t / (timeSpan * timeSpan)))
    done
  }

  override def getSpeed(timeNow: Double) = {
    val t = timeNow - timeStart
    if (animateVectorType == AnimateVectorType.PositionLike.id && !isDone(timeNow))
      (getDelta * 2 / timeSpan) * t / timeSpan
    else new VecDouble(getVector.getSize)
  }
}


class AnimateSmooth[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Smooth.id, animateVectorType, timeStart, timeSpan) {

  override def update(timeNow: Double) = {
    val t = timeNow - timeStart
    val done = isDone(timeNow)
    if (done) getVector.set(vecTarget)
    else if (t < timeSpan / 2)
      getVector.set(getSource + getDelta * (2 * t * t / (timeSpan * timeSpan)))
    else
      getVector.set(getSource + getDelta * (-1 - 2 * t * t / (timeSpan * timeSpan) + 4 * t / timeSpan))
    done
  }

  override def getSpeed(timeNow: Double) = {
    val t = timeNow - timeStart
    if (animateVectorType == AnimateVectorType.PositionLike.id && !isDone(timeNow))
      (getDelta * 2 / timeSpan) * (
        if (t < timeSpan / 2) t / timeSpan
        else 1 - t / timeSpan
        )
    else new VecDouble(getVector.getSize)
  }
}

// class AnimateNonLinearToTargetScale[V <: VecDouble]
// (vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, speedMax: Double, a: Double)
//   extends AnimateNonLinearToTargetVec(vecSource, vecTarget, animateVectorType, timeStart, timeSpan, VecDouble.toDirection(vecSource, vecTarget, speedMax), VecDouble.toDirection(vecSource, vecTarget, a))
