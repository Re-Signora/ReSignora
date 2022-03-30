package work.chiro.game.animate

import work.chiro.game.basic.PositionType.Scale
import work.chiro.game.basic.VecDouble
import work.chiro.game.logger

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

  def isDone(timeNow: Double) = {
    val done = timeNow > timeStart + timeSpan
    // if (done) logger.info(f"done!!")
    done
  }

  def getSpeed(timeNow: Double): VecDouble
}

class AnimateLinearToTarget[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, willStop: Boolean = true)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Linear.id, animateVectorType, timeStart, timeSpan) {

  // logger.info(s"Animate Linear $vecSource => $vecTarget")
  override def isDone(timeNow: Double) = if (willStop) super.isDone(timeNow) else false

  override def update(timeNow: Double) = {
    val done = isDone(timeNow)
    val deltaNew = getDelta * ((timeNow - timeStart) / timeSpan)
    // logger.info(f"delta = $delta deltaNew = $deltaNew")
    if (done) getVector.set(vecTarget)
    else getVector.set(getSource + deltaNew)
    done
  }

  override def getSpeed(timeNow: Double) =
    if (animateVectorType == AnimateVectorType.PositionLike.id) getDelta / timeSpan
    else new VecDouble(getVector.getSize)
}

class AnimateSmooth[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, speedMax: Double, a: Double, willStop: Boolean = true, r: Double = 1)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.Smooth.id, animateVectorType, timeStart, timeSpan) {

  override def isDone(timeNow: Double) =
    if (willStop)
    // super.isDone(timeNow)
      timeNow - timeStart > timeNonLinear * 2 + timeLinear
    else false

  val unit = getDelta / getDelta.scale
  val aVec = unit * a
  val timeNonLinear = math.sqrt(getDelta.scale * r / ((r + 1) * a)) / 2
  val timeLinear = 2 * math.sqrt(getDelta.scale / (a * r * (r + 1)))
  // val timeLinear = timeSpan - timeNonLinear * 2

  def vMax = a * timeNonLinear

  def getX1 = getDelta.scale * (r / (r + 1))

  def getX2 = getDelta.scale / (r + 1)

  // def vMax = getX2 / timeSpan

  // logger.info(f"distance = ${getDelta.scale}%.3f, x1 = $getX1%.3f, x2 = $getX2%.3f")
  // logger.info(f"t1 = $timeNonLinear, t2 = $timeLinear")
  // logger.info(s"unit = $unit")

  override def update(timeNow: Double) = {
    val t = timeNow - timeStart
    // val scale = 1 + r / (r + 1)
    val scale = 1
    val done = isDone(timeNow)
    // if (done) logger.info(f"done!!")
    if (done) getVector.set(vecTarget)
    else {
      if (t < timeNonLinear)
      // getVector.set(vecSource)
        getVector.set(getSource + aVec * (t * t / 2) * scale)
      else if (t < timeNonLinear + timeLinear) {
        getVector.set(getSource + (aVec * (timeNonLinear * timeNonLinear / 2) +
          unit * vMax * (t - timeNonLinear)) * scale)
        // getVector.set(vecSource)
      } else {
        if (t < timeNonLinear * 2 + timeLinear) {
          getVector.set(getSource + (aVec * (timeNonLinear * timeNonLinear) +
            unit * vMax * timeLinear -
            aVec * ((timeLinear + 2 * timeNonLinear - t) * (timeLinear + 2 * timeNonLinear - t) / 2)) * scale
          )
        } else {
          getVector.set(vecTarget)
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
        else speedMax
        )
    else new VecDouble(getVector.getSize)
  }
}

class AnimateNonLinear[V <: VecDouble]
(vecSource: V, vecTarget: V, animateVectorType: Int, timeStart: Double, timeSpan: Double, willStop: Boolean = true)
  extends AbstractAnimate(vecSource, vecTarget, AnimateType.NonLinear.id, animateVectorType, timeStart, timeSpan) {

  override def isDone(timeNow: Double) =
    if (willStop)
      super.isDone(timeNow)
    else false

  // val unit = getDelta / getDelta.scale
  val a = getDelta * 2 / (timeSpan * timeSpan)

  override def update(timeNow: Double) = {
    val t = timeNow - timeStart
    val done = isDone(timeNow)
    if (done) logger.info(f"done!!")
    if (done) getVector.set(vecTarget)
    else {
      if (t < timeSpan / 2)
        getVector.set(getSource + getDelta * (2 * t * t / (timeSpan * timeSpan)))
      else {
        getVector.set(getSource + getDelta * (-1 - 2 * t * t / (timeSpan * timeSpan) + 4 * t / timeSpan))
        // logger.info(f"stage 2: $getVector")
      }
    }
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
