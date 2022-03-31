package work.chiro.game.animate

import work.chiro.game.basic.VecDouble

class AnimateTypeEnumeration extends Enumeration

object AnimateType extends AnimateTypeEnumeration {
  val Unknown, Linear, LinearToTarget, NonLinear, SmoothTo, LinearLoop = Value
}

class AnimateVectorTypeEnumeration extends Enumeration

object AnimateVectorType extends AnimateVectorTypeEnumeration {
  val PositionLike, Others = Value
}

trait AnimateWithTarget {
  def getSource: VecDouble

  def getTarget: VecDouble

  var delta: Option[VecDouble] = None

  def getDeltaTarget = {
    if (delta.isEmpty) delta = Some(getDeltaDynamic)
    delta.get
  }

  def getDeltaDynamic = getTarget - getSource
}

abstract class AbstractAnimate[V <: VecDouble]
(vecSource: V, animateType: Int, animateVectorType: Int) {
  def getAnimateType: Int = animateType

  def getAnimateVectorType: Int = animateVectorType

  def getVector: V = vec

  private val vec = vecSource
  private val source = vecSource.copy

  def getSource = source

  def update(timeNow: Double): Boolean

  def isDone(timeNow: Double): Boolean

  def getSpeed(timeNow: Double): VecDouble

  def getDelta: VecDouble
}

class AnimateLinearToTarget[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, willStop: Boolean = true)
  extends AbstractAnimate(vecSource, AnimateType.LinearToTarget.id, animateVectorType)
    with AnimateWithTarget {

  override def isDone(timeNow: Double) = if (willStop && timeSpan > 0) timeNow > timeStart + timeSpan else false

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

  override def getTarget = vecTarget

  override def getDelta = getDeltaTarget
}

class AnimateLinear[V <: VecDouble]
(vecSource: V, speed: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, willStop: Boolean = true)
  extends AbstractAnimate(vecSource, AnimateType.Linear.id, animateVectorType) {

  override def isDone(timeNow: Double) = if (willStop && timeSpan > 0) timeNow > timeStart + timeSpan else false

  override def update(timeNow: Double) = {
    val done = isDone(timeNow)
    val deltaNew = speed * (timeNow - timeStart)
    getVector.set(getSource + deltaNew)
    done
  }

  override def getSpeed(timeNow: Double) =
    if (animateVectorType == AnimateVectorType.PositionLike.id) speed
    else new VecDouble(getVector.getSize)

  override def getDelta = new VecDouble(getVector.getSize)
}

class AnimateLinearLoop[V <: VecDouble]
(vecSource: V, speed: V, animateVectorType: Int, timeStart: Double, posRange: V)
  extends AbstractAnimate(vecSource, AnimateType.LinearLoop.id, animateVectorType) {
  override def update(timeNow: Double) = {
    getVector.set(getSource + speed * (timeNow - timeStart))
    getVector.set(for {i <- 0 until getVector.getSize} yield {
      getVector.get(i) % posRange.get(i)
    })
    false
  }

  override def isDone(timeNow: Double) = false

  override def getSpeed(timeNow: Double) = speed

  override def getDelta = new VecDouble(getVector.getSize)
}

class AnimateNonLinear[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, willStop: Boolean = true)
  extends AbstractAnimate(vecSource, AnimateType.NonLinear.id, animateVectorType)
    with AnimateWithTarget {

  override def isDone(timeNow: Double) = if (willStop && timeSpan > 0) timeNow > timeStart + timeSpan else false

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

  override def getTarget = vecTarget

  override def getDelta = getDeltaTarget
}


class AnimateSmoothTo[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double)
  extends AbstractAnimate(vecSource, AnimateType.SmoothTo.id, animateVectorType)
    with AnimateWithTarget {

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

  override def getTarget = vecTarget

  override def getDelta = getDeltaTarget

  override def isDone(timeNow: Double) = timeNow > timeStart + timeSpan
}
