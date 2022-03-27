package work.chiro.game.basic

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.basic.PositionType.Position

abstract class FlyingObject(posInit: Position, animateContainer: AnimateContainer[Vec2Double], size: Option[Vec2Double] = None)
  extends BasicObject(posInit, animateContainer, size = size) {

}
