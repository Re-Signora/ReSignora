package work.chiro.game.layer

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.ScaleType.{Position, SizeDouble}
import work.chiro.game.basic.{AbstractObject, AbstractObjectFactory}

import java.awt.Graphics

abstract class AbstractLayer(posInit: Position,
                             animateContainer: AnimateContainer,
                             sizeInit: Option[SizeDouble] = None)
  extends AbstractObject(
    posInit,
    animateContainer,
    sizeInit = sizeInit) {

  override def draw(g: Graphics) = super.draw(g, alignCenter = false)
}

trait AbstractLayerFactory extends ImageResourceFactory with AbstractObjectFactory {
  override def create(): AbstractLayer
}