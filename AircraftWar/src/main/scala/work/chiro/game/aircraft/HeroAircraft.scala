package work.chiro.game.aircraft

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.{AnimateContainer, AnimateLinear, AnimateLinearToTarget, AnimateVectorType}
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.PositionType.Position
import work.chiro.game.basic.{AbstractObjectFactory, Vec2Double}
import work.chiro.game.bullet.HeroBullet
import work.chiro.game.utils.getTimeMills

/**
 * 英雄飞机，游戏玩家操控
 *
 * @param posInit          初始坐标
 * @param animateContainer 动画容器
 * @param hpInit           初始血量
 */
class HeroAircraft(posInit: Position, animateContainer: AnimateContainer[Vec2Double], hpInit: Int)
  extends AbstractAircraft(posInit, animateContainer, hpInit, dInit = Some(config.hero.box)) {
  // 攻击方式
  //子弹一次发射数量
  private var shootNum = 1
  //子弹伤害级别
  private var powerStep = 0

  // 英雄机由鼠标控制，不通过forward函数移动
  override def forward() = {}

  def setShootNum(num: Int) = shootNum = num

  def getShootNum = shootNum

  def increasePower() = {
    if (getShootNum < 3) setShootNum(getShootNum + 1)
    else powerStep = math.min(powerStep + 1, config.hero.powerSteps.length - 1)
  }

  def reachMaxPower = powerStep == config.hero.powerSteps.length - 1

  /**
   * 通过射击产生子弹
   *
   * @return 射击出的子弹List
   */
  override def shoot() = {
    val x = getLocationX
    val y = getLocationY
    for {i <- 0 until shootNum} yield {
      val posNew = new Position(x + (i * 2 - shootNum + 1) * 10, y)
      new HeroBullet(posNew, new AnimateContainer[Position](List(
        // new AnimateLinearToTarget(posNew, new Position(posNew.getX, 0), AnimateVectorType.PositionLike.id, getTimeMills, 300)
        new AnimateLinear(posNew, new Position(0, -3), AnimateVectorType.PositionLike.id, getTimeMills, 0)
      )), config.hero.powerSteps(powerStep))
    }
  }

  override def getImage = HeroAircraft.getImage
}

object HeroAircraft extends ImageResourceFactory with AbstractObjectFactory {
  object HeroBox extends ImageResourceFactory {
    override def getImageCachedPath = "images/box_hero.png"
  }
  override def getImageCachedPath = "images/hero.png"

  var heroInstance: Option[HeroAircraft] = None
  var heroPositionInstance: Option[Position] = None

  def getInstance = heroInstance.get

  def getPositionInstance = heroPositionInstance.get

  def create() = {
    if (heroInstance.isEmpty) {
      heroPositionInstance = Some(new Position(config.window.width / 2, config.window.height - HeroAircraft.getImage.getHeight))
      heroInstance.synchronized({
        heroInstance = Some(new HeroAircraft(heroPositionInstance.get, new AnimateContainer[Position], 100))
      })
    }
    heroInstance.get
  }
}