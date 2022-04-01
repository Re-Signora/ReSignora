package work.chiro.game.application

import org.luaj.vm2.LoadState
import org.luaj.vm2.compiler.LuaC
import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.aircraft._
import work.chiro.game.application.Main.getLuaGlobals
import work.chiro.game.basic.AbstractObject
import work.chiro.game.bullet.AbstractBullet
import work.chiro.game.control.HeroController
import work.chiro.game.layer.{AbstractLayer, Background}
import work.chiro.game.libraries.LibrariesLoader
import work.chiro.game.logger
import work.chiro.game.prop.{AbstractProp, BloodProp, BombProp, BulletProp}
import work.chiro.game.utils.getTimeMills

import java.awt.{Color, Font, Graphics}
import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}
import javax.swing._
import scala.collection.mutable.ListBuffer

/**
 * 游戏主面板，游戏启动
 *
 * @author chiro2001
 */
class Game(frame: JFrame) extends JPanel {
  logger.info(s"Window(${config.window.width}x${config.window.height}), Playground(${config.window.playWidth}x${config.window.playHeight})")
  val heroAircraft = HeroAircraft.create()
  val heroPosition = HeroAircraft.getPositionInstance
  val gameBackground = Background.create()
  val enemyAircrafts = new ListBuffer[AbstractAircraft]
  val heroBullets = new ListBuffer[AbstractBullet]
  val enemyBullets = new ListBuffer[AbstractBullet]
  val props = new ListBuffer[AbstractProp]
  val backgrounds = new ListBuffer[AbstractObject].addOne(gameBackground)
  val heroAircrafts = new ListBuffer[AbstractAircraft].addOne(heroAircraft)
  val aircraftBoxes = new ListBuffer[AbstractObject].addOne(HeroAircraft.getInstance.box)
  val layers = new ListBuffer[AbstractLayer]
  val allObjectLists = Array(
    enemyAircrafts,
    heroBullets,
    enemyBullets,
    props,
    backgrounds,
    layers
  )
  val allObjectsToDraw = Array(
    backgrounds,
    heroBullets,
    enemyAircrafts,
    heroAircrafts,
    enemyBullets,
    props,
    aircraftBoxes,
    layers
  )
  val allAircrafts = Array(enemyAircrafts, List(heroAircraft))
  val allBullets = Array(heroBullets, enemyBullets)
  // Scheduled 线程池，用于定时任务调度
  val executorService = new ScheduledThreadPoolExecutor(1)
  // 启动英雄机控制监听
  val controller = new HeroController(frame, this, heroAircraft)
  // private var backGroundTop = 0
  private val timeInterval = 1
  private var gameOverFlag = false
  private var score = 0

  private var frameRenderTime: Double = 0
  private var lastFrameRenderTime: Double = 0

  private var frameCalcTime: Double = 0
  private var lastFrameCalcTime: Double = 0

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
  private val frameRenderCount = new ListBuffer[Double]
  private val frameCalcCount = new ListBuffer[Double]

  def frameCalcTimeDelta = frameCalcTime - lastFrameCalcTime

  /**
   * 游戏启动入口，执行游戏逻辑
   */
  def action() = {
    enemyAircrafts.synchronized(enemyAircrafts.append(EliteEnemy.create()))
    // 定时任务：对象产生、碰撞判定、击毁及结束判定
    val calcTask = new Runnable {
      override def run() = {
        frameCalcTime = getTimeMills
        frameCalcCount.append(frameCalcTime)
        frameCalcCount.filterInPlace(_ >= (if (frameRenderTime >= 1000) frameCalcTime - 1000 else 0))
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
          val fpsInfo = f"[ Calc ${frameCalcCount.size} fps | Render ${frameRenderCount.size} fps ]"
          if (config.running.showFps) logger.info(fpsInfo)
          Main.getFrameInstance.setTitle(f"Aircraft War $fpsInfo")
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
        lastFrameCalcTime = frameCalcTime
      }
    }

    val renderTask = new Runnable {
      override def run() = {
        frameRenderTime = getTimeMills
        frameRenderCount.append(frameRenderTime)
        frameRenderCount.filterInPlace(_ >= (if (frameRenderTime >= 1000) frameRenderTime - 1000 else 0))
        // 重绘界面
        repaint()
        lastFrameRenderTime = frameRenderTime
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
    // 加载自定义库
    LibrariesLoader.loadAllLibraries()
    LoadState.install(getLuaGlobals)
    LuaC.install(getLuaGlobals)
    getLuaGlobals.loadfile("scripts/init.lua").call()
    logger.info("init.lua launched.")
    getLuaGlobals.loadfile("scripts/game.lua").call()
    logger.info("game.lua launched.")
  }

  private def onHeroShootCountCycle = {
    heroShootCycleTime += frameCalcTimeDelta
    // 跨越到新的周期
    if (heroShootCycleTime >= heroShootDuration) {
      heroShootCycleTime %= heroShootDuration
      true
    } else false
  }

  private def onEliteShootCountCycle = {
    eliteShootCycleTime += frameCalcTimeDelta
    // 跨越到新的周期
    if (eliteShootCycleTime >= eliteShootDuration) {
      eliteShootCycleTime %= eliteShootDuration
      true
    } else false
  }

  private def onMobCreateCountCycle = {
    mobCreateCycleTime += frameCalcTimeDelta
    // 跨越到新的周期
    if (mobCreateCycleTime >= mobCreateDuration) {
      mobCreateCycleTime %= mobCreateDuration
      true
    } else false
  }

  private def onEliteCreateCountCycle = {
    eliteCreateCycleTime += frameCalcTimeDelta
    // 跨越到新的周期
    if (eliteCreateCycleTime >= eliteCreateDuration) {
      eliteCreateCycleTime %= eliteCreateDuration
      true
    } else false
  }

  private def onFpsCountCycle = {
    fpsCycleTime += frameCalcTimeDelta
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
      if (bullet.crash(heroAircraft)) {
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
            if (enemyAircraft.crash(heroAircraft)) {
              enemyAircraft.vanish()
              heroAircraft.decreaseHp(Integer.MAX_VALUE)
            }
          }
        })
    })
    // 我方获得道具
    props.foreach(prop => if (prop.isValid) {
      if (prop.crash(heroAircraft)) {
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

    // 绘制所有物体
    allObjectsToDraw.foreach(objList => objList.synchronized(objList.foreach(_.draw(g))))
    // 绘制得分和生命值
    paintScoreAndLife(g)
    // 绘制边框防止图像溢出
    paintOuterMask(g)
  }

  private def paintOuterMask(g: Graphics) = {
    g.setColor(new Color(config.window.bgColor))
    g.fillRect(0, 0, config.window.playOffsetX, config.window.height)
    g.fillRect(config.window.playOffsetX, 0, config.window.playWidth, config.window.playOffsetY)
    g.fillRect(config.window.playOffsetX + config.window.playWidth, 0, config.window.width, config.window.height)
    g.fillRect(config.window.playOffsetX, config.window.playHeight, config.window.playWidth, config.window.height)
  }

  private def paintScoreAndLife(g: Graphics) = {
    val x = 10 + config.window.playOffsetX
    var y = 25 + config.window.playOffsetY
    g.setColor(new Color(16711680))
    g.setFont(new Font("SansSerif", Font.BOLD, 22))
    g.drawString("SCORE:" + this.score, x, y)
    y = y + 20
    g.drawString("LIFE:" + this.heroAircraft.getHp, x, y)
  }
}