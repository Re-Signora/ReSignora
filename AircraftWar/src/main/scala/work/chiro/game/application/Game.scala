package work.chiro.game.application

import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft._
import work.chiro.game.basic.AbstractObject
import work.chiro.game.bullet.AbstractBullet
import work.chiro.game.control.HeroController
import work.chiro.game.logger
import work.chiro.game.prop.{AbstractProp, BloodProp, BombProp, BulletProp}
import work.chiro.game.scene.Background
import work.chiro.game.utils.getTimeMills

import java.awt.geom.AffineTransform
import java.awt.{Color, Font, Graphics, Graphics2D}
import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}
import javax.swing._
import scala.collection.mutable.ListBuffer

/**
 * 游戏主面板，游戏启动
 *
 * @author chiro2001
 */
class Game(frame: JFrame) extends JPanel {
  logger.info(s"Window(${config.window.width}x${config.window.height})")
  val heroAircraft = HeroAircraft.create()
  val heroPosition = HeroAircraft.getPositionInstance
  val enemyAircrafts = new ListBuffer[AbstractAircraft]
  val heroBullets = new ListBuffer[AbstractBullet]
  val enemyBullets = new ListBuffer[AbstractBullet]
  val props = new ListBuffer[AbstractProp]
  val allObjectLists = Array(
    enemyAircrafts,
    heroBullets,
    enemyBullets,
    props
  )
  val allAircrafts = Array(enemyAircrafts, List(heroAircraft))
  val allBullets = Array(heroBullets, enemyBullets)
  // Scheduled 线程池，用于定时任务调度
  val executorService = new ScheduledThreadPoolExecutor(1)
  // 启动英雄机控制监听
  val controller = new HeroController(frame, this, heroAircraft)
  private var backGroundTop = 0
  private val timeInterval = 1
  private var gameOverFlag = false
  private var score = 0

  private var frameTime: Double = 0
  private var lastFrameTime: Double = 0

  private val heroShootDuration = 1
  private var heroShootCycleTime: Double = 0

  private val mobCreateDuration = 1200
  private var mobCreateCycleTime: Double = 0

  private val eliteCreateDuration = 600
  private var eliteCreateCycleTime: Double = 0

  private val eliteShootDuration = 200
  private var eliteShootCycleTime: Double = 0

  private var fpsCycleTime: Double = 0
  private val fpsCycleDuration = 100
  private val frameCount = new ListBuffer[Double]

  def frameTimeDelta = frameTime - lastFrameTime

  /**
   * 游戏启动入口，执行游戏逻辑
   */
  def action() = {
    enemyAircrafts.synchronized(enemyAircrafts.append(EliteEnemy.create()))
    // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
    val calcTask = new Runnable {
      override def run() = {
        // 周期性执行（控制频率）
        // 键盘操作
        controller.onFrame()
        // 英雄飞机射出子弹
        if (onHeroShootCountCycle) heroShootAction()
        // 敌机射出子弹
        if (onEliteShootCountCycle) eliteShootAction()
        // 新敌机产生
        if (onMobCreateCountCycle) enemyAircrafts.synchronized(enemyAircrafts.append(MobEnemy.create()))
        // 产生精英敌机
        if (onEliteCreateCountCycle) enemyAircrafts.synchronized(enemyAircrafts.append(EliteEnemy.create()))
        if (onFpsCountCycle) {
          frameCount.synchronized({
            val fpsInfo = f"[ ${frameTime / 1000}%.3fs ] ${frameCount.size} fps"
            if (config.running.showFps) logger.info(fpsInfo)
            Main.getFrameInstance.get.setTitle(f"Aircraft War $fpsInfo")
          })
        }
        // 所有物体移动
        allObjectLists.foreach(_.foreach(_.forward()))
        // 撞击检测
        crashCheckAction()
        // 后处理
        postProcessAction()
        // 游戏结束检查
        if (!config.isDebug && heroAircraft.getHp <= 0) { // 游戏结束
          executorService.shutdown()
          gameOverFlag = true
          logger.info("Game Over!")
        }
        lastFrameTime = frameTime
      }
    }

    val renderTask = new Runnable {
      override def run() = {
        frameTime = getTimeMills
        frameCount.synchronized({
          frameCount.append(frameTime)
          frameCount.filterInPlace(_ >= (if (frameTime >= 1000) frameTime - 1000 else 0))
        })
        // 重绘界面
        repaint()
      }
    }

    /**
     * 以固定延迟时间进行执行
     * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
     */
    executorService.scheduleWithFixedDelay(calcTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS)
    executorService.scheduleWithFixedDelay(renderTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS)
    // 不管上次执行是否完成也执行
    // executorService.scheduleAtFixedRate(calcTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS)
  }

  private def onHeroShootCountCycle = {
    heroShootCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (heroShootCycleTime >= heroShootDuration) {
      heroShootCycleTime %= heroShootDuration
      true
    } else false
  }

  private def onEliteShootCountCycle = {
    eliteShootCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (eliteShootCycleTime >= eliteShootDuration) {
      eliteShootCycleTime %= eliteShootDuration
      true
    } else false
  }

  private def onMobCreateCountCycle = {
    mobCreateCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (mobCreateCycleTime >= mobCreateDuration) {
      mobCreateCycleTime %= mobCreateDuration
      true
    } else false
  }

  private def onEliteCreateCountCycle = {
    eliteCreateCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (eliteCreateCycleTime >= eliteCreateDuration) {
      eliteCreateCycleTime %= eliteCreateDuration
      true
    } else false
  }

  private def onFpsCountCycle = {
    fpsCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (fpsCycleTime >= fpsCycleDuration) {
      // logger.info(f"bullet count: ${allBullets.map(_.size).sum}")
      fpsCycleTime %= fpsCycleDuration
      true
    } else false
  }

  private def heroShootAction() = {
    // 英雄射击
    if (controller.pressed(config.control.keyShoot))
      heroBullets.synchronized(heroBullets.addAll(heroAircraft.shoot()))
  }

  private def eliteShootAction() = {
    // 精英敌机射击
    enemyAircrafts.foreach(enemy => enemyBullets.synchronized(enemyBullets.addAll(enemy.shoot())))
  }

  /**
   * 碰撞检测：
   * 1. 敌机攻击英雄
   * 2. 英雄攻击/撞击敌机
   * 3. 英雄获得补给
   */
  private def crashCheckAction() = {
    // 敌机子弹攻击英雄
    enemyBullets.foreach(bullet => if (bullet.isValid) {
      if (heroAircraft.crash(bullet)) {
        heroAircraft.decreaseHp(bullet.getPower)
        bullet.vanish()
      }
    })
    // 英雄子弹攻击敌机
    heroBullets.foreach(bullet => {
      if (bullet.isValid)
        enemyAircrafts.foreach(enemyAircraft => {
          if (enemyAircraft.isValid) { // 已被其他子弹击毁的敌机，不再检测
            // 避免多个子弹重复击毁同一敌机的判定
            if (enemyAircraft.crash(bullet)) { // 敌机撞击到英雄机子弹
              // 敌机损失一定生命值
              enemyAircraft.decreaseHp(bullet.getPower)
              bullet.vanish()
              if (!enemyAircraft.isValid) {
                // 获得分数
                score += 10
                if (enemyAircraft.getClass.getName.endsWith("EliteEnemy")) {
                  // 产生道具补给
                  if (math.random() < 0.7) {
                    props.synchronized(props.append((math.random() * 3).toInt match {
                      case 0 => BulletProp.create(enemyAircraft.getPos)
                      case 1 => BombProp.create(enemyAircraft.getPos)
                      case 2 => BloodProp.create(enemyAircraft.getPos)
                    }))
                  }
                }
              }
            }
            // 英雄机 与 敌机 相撞，均损毁
            if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
              enemyAircraft.vanish()
              heroAircraft.decreaseHp(Integer.MAX_VALUE)
            }
          }
        })
    })
    // 我方获得道具
    props.foreach(prop => if (prop.isValid) {
      if (prop.crash(heroAircraft, useImageBox = true)) {
        // 道具生效
        prop.handleAircrafts(enemyAircrafts.toList)
        prop.vanish()
      }
    })
  }

  /**
   * 后处理：
   * 1. 删除无效的子弹
   * 2. 删除无效的敌机
   * 3. 检查英雄机生存
   * 无效的原因可能是撞击或者飞出边界
   */
  private def postProcessAction() = {
    allObjectLists.foreach(objList => objList.synchronized(objList.filterInPlace(_.isValid)))
  }

  /**
   * 重写paint方法
   * 通过重复调用paint方法，实现游戏动画
   */
  override def paint(g: Graphics) = {
    super.paint(g)
    // 绘制背景,图片滚动
    g.drawImage(Background.getImage, 0, backGroundTop - config.window.height, null)
    g.drawImage(Background.getImage, 0, backGroundTop, null)
    // backGroundTop += 1
    if (backGroundTop == config.window.height) backGroundTop = 0
    // 先绘制子弹，后绘制飞机
    // 这样子弹显示在飞机的下层
    paintImageWithPositionRevised(g, heroBullets)
    paintImageWithPositionRevised(g, enemyAircrafts)
    g.drawImage(
      HeroAircraft.getImage,
      (heroAircraft.getLocationX - HeroAircraft.getImage.getWidth / 2).toInt,
      (heroAircraft.getLocationY - HeroAircraft.getImage.getHeight / 2).toInt,
      null
    )
    paintImageWithPositionRevised(g, enemyBullets)
    paintImageWithPositionRevised(g, props)
    g.drawImage(
      HeroAircraft.HeroBox.getImage,
      (heroAircraft.getLocationX - config.hero.box / 2).toInt,
      (heroAircraft.getLocationY - config.hero.box / 2).toInt,
      config.hero.box,
      config.hero.box,
      null
    )
    //绘制得分和生命值
    paintScoreAndLife(g)
  }

  private def paintImageWithPositionRevised(g: Graphics, objects: ListBuffer[_ <: AbstractObject]): Unit = {
    objects.synchronized {
      objects.foreach(obj => {
        val image = obj.getImage
        if (obj.rotation.getX == 0) {
          g.drawImage(image, (obj.getLocationX - image.getWidth / 2).toInt, (obj.getLocationY - image.getHeight / 2).toInt, null)
        } else {
          val af = AffineTransform.getTranslateInstance(obj.getLocationX - image.getWidth / 2, obj.getLocationY - image.getHeight / 2)
          af.rotate(obj.rotation.getX, image.getWidth / 2, image.getHeight / 2)
          val graphics2d = g.asInstanceOf[Graphics2D]
          graphics2d.drawImage(image, af, null)
        }
      })
    }
  }

  private def paintScoreAndLife(g: Graphics) = {
    val x = 10
    var y = 25
    g.setColor(new Color(16711680))
    g.setFont(new Font("SansSerif", Font.BOLD, 22))
    g.drawString("SCORE:" + this.score, x, y)
    y = y + 20
    g.drawString("LIFE:" + this.heroAircraft.getHp, x, y)
  }
}