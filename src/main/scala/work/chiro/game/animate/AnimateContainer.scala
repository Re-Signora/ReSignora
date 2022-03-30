package work.chiro.game.animate

import work.chiro.game.basic.PositionType.Scale
import work.chiro.game.basic.VecDouble

class AnimateContainer[V <: VecDouble]
(animateSeq: List[AbstractAnimate[V]] = List()) {
  def getAnimateSeq = animateSeq

  protected def updateAllInner(timeNow: Double) = animateSeq.map(_.update(timeNow))

  def updateAll(timeNow: Double) = updateAllInner(timeNow).map(if (_) 0 else 1).sum == 0

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
