package work.chiro.game.basic

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.basic.PositionType.{Position, Scale, Size}

abstract class AbstractFlyingObject(posInit: Position,
                                    animateContainer: AnimateContainer[Vec2Double],
                                    sizeInit: Option[Size] = None,
                                    rotationInit: Option[Scale] = None)
  extends AbstractObject(posInit, animateContainer, sizeInit = sizeInit, rotationInit = rotationInit)
