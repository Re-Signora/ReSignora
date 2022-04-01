package work.chiro.game.basic

import work.chiro.game.animate.AnimateContainer
import work.chiro.game.basic.ScaleType.{Position, Scale, SizeDouble}

abstract class AbstractFlyingObject(posInit: Position,
                                    animateContainer: AnimateContainer,
                                    sizeInit: Option[SizeDouble] = None,
                                    rotationInit: Option[Scale] = None)
  extends AbstractObject(posInit, animateContainer, sizeInit = sizeInit, rotationInit = rotationInit)
