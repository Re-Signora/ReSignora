package work.chiro.game.aircraft

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.animate.{AnimateContainer, AnimateLinear, AnimateVectorType}
import work.chiro.game.application.ImageResourceFactory
import work.chiro.game.basic.ScaleType.{Position, Scale, Size, SizeDouble}
import work.chiro.game.basic.{AbstractObject, AbstractObjectFactory}
import work.chiro.game.bullet.HeroBullet
import work.chiro.game.control.HeroController
import work.chiro.game.logger
import work.chiro.game.utils.getTimeMills

import java.awt.Graphics

/**
 * 英雄飞机，游戏玩家操控
 *
 * @param posInit          初始坐标
 * @param animateContainer 动画容器
 * @param hpInit           初始血量
 */
class HeroAircraft(posInit: Position,
                   animateContainer: AnimateContainer,
                   hpInit: Int,
                   sizeInit: Option[SizeDouble] = None,
                   rotationInit: Option[Scale] = None)
  extends AbstractAircraft(posInit, animateContainer, hpInit, sizeInit = sizeInit, rotationInit = rotationInit) {
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
    if (getShootNum < 3) {
      logger.info(s"shootNum increased to $shootNum!")
      setShootNum(getShootNum + 1)
    }
    else {
      if (powerStep == 1) logger.info("enable bullet scattering!")
      powerStep = math.min(powerStep + 1, config.hero.powerSteps.length - 1)
    }
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
    if (powerStep <= 1) {
      for {i <- 0 until shootNum} yield {
        val posNew = new Position(x + (i * 2 - shootNum + 1) * 10, y)
        new HeroBullet(posNew, new AnimateContainer(List(
          new AnimateLinear(posNew, new Position(0, -3), AnimateVectorType.PositionLike.id, getTimeMills, 0)
        )), config.hero.powerSteps(powerStep))
      }
    } else {
      for {i <- 0 until 3} yield {
        val posNew = getPos.copy
        new HeroBullet(posNew, new AnimateContainer(List(
          new AnimateLinear(posNew, new Position((i - 1) * 0.3, -3), AnimateVectorType.PositionLike.id, getTimeMills, 0)
        )), config.hero.powerSteps(powerStep))
      }
    }
  }

  override def getImage = HeroAircraft.getImage

  val box = new HeroAircraftBox(getPos)

  override def draw(g: Graphics) = super.draw(g, sizeDraw = Some(new Size(getImage.getWidth, getImage.getHeight)))

  override def getBoxSize = box.getSize
}

/**
 * 英雄机碰撞盒
 * @param posInit          初始位置
 */
class HeroAircraftBox(posInit: Position)
  extends AbstractObject(posInit, new AnimateContainer(), sizeInit = Some(new Size(config.hero.box, config.hero.box))) {
  override def getImage = HeroAircraftBox.getImage

  override def draw(g: Graphics) = {
    if (HeroController.getInstance.pressed(config.control.keySlow))
      super.draw(g, position = Some(getPos - new Position(config.hero.box / 2, config.hero.box / 2)), alignCenter = false)
  }
}

object HeroAircraftBox extends ImageResourceFactory {
  override def getImageCachedPath = "images/box_hero.png"
}

object HeroAircraft extends ImageResourceFactory with AbstractObjectFactory {
  override def getImageCachedPath = "images/hero.png"

  var heroInstance: Option[HeroAircraft] = None
  var heroPositionInstance: Option[Position] = None

  def getInstance = heroInstance.get

  def getPositionInstance = heroPositionInstance.get

  def create() = {
    if (heroInstance.isEmpty) {
      heroPositionInstance = Some(new Position(config.window.playWidth / 2, config.window.playHeight - HeroAircraft.getImage.getHeight))
      heroInstance.synchronized({
        heroInstance = Some(new HeroAircraft(heroPositionInstance.get, new AnimateContainer, 100))
      })
    }
    heroInstance.get
  }
}