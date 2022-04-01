package work.chiro.game.layer

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.{AnimateContainer, AnimateLinearLoop, AnimateVectorType}
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.PositionType.{Position, Size, SizeDouble}
import work.chiro.game.basic.Vec2Double
import work.chiro.game.utils.getTimeMills

import java.awt.Graphics

class Background(posInit: Position, animateContainer: AnimateContainer[Vec2Double])
  extends AbstractLayer(
    posInit,
    animateContainer,
    sizeInit = Some(new Size(config.window.playWidth, config.window.playHeight))) {
  override def getImage = Background.getImage

  override def draw(g: Graphics) = {
    super.draw(g, alignCenter = false)
    val newPos = getPos + new Position(0, -config.window.playHeight)
    newPos.setY(newPos.getY % config.window.playHeight)
    super.draw(g, img = Some(getImage), position = Some(newPos), alignCenter = false)
  }
}

object Background extends ImageResourceFactory with AbstractLayerFactory {
  override def getImageCachedPath = "images/bg.jpg"

  private var backgroundInstance: Option[Background] = None
  private var backgroundPositionInstance: Option[Position] = None
  private val windowRange = new SizeDouble(config.window.playWidth, config.window.playHeight)

  def getInstance = backgroundInstance.get

  def getPositionInstance = backgroundPositionInstance.get

  override def create() =
    if (backgroundInstance.nonEmpty) backgroundInstance.get
    else {
      backgroundPositionInstance = Some(new Position(0, 0))
      backgroundInstance = Some(new Background(getPositionInstance, new AnimateContainer(List(
        new AnimateLinearLoop(getPositionInstance, new Position(0, 0.05), AnimateVectorType.PositionLike.id, getTimeMills, windowRange)
      ))))
      getInstance
    }
}