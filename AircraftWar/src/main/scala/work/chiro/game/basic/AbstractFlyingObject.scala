package work.chiro.game.basic

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.basic.PositionType.Position

abstract class AbstractFlyingObject(posInit: Position, animateContainer: AnimateContainer[Vec2Double], size: Option[Vec2Double] = None)
  extends AbstractObject(posInit, animateContainer, size = size) {

}
