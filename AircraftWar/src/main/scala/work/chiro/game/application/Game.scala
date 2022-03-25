package work.chiro.game.application

import work.chiro.game.aircraft._
import work.chiro.game.basic.FlyingObject
import work.chiro.game.bullet.AbstractBullet
import work.chiro.game.prop.{AbstractProp, BloodProp}
import work.chiro.game.scene.Background
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
class Game extends JPanel {
  println(s"Window(${Main.WINDOW_WIDTH}x${Main.WINDOW_HEIGHT})")
  val heroAircraft = HeroAircraft.create()
  val heroPosition = HeroAircraft.getHeroPositionInstance
  val enemyAircrafts = new ListBuffer[AbstractAircraft]
  val heroBullets = new ListBuffer[AbstractBullet]
  val enemyBullets = new ListBuffer[AbstractBullet]
  val props = new ListBuffer[AbstractProp]
  // Scheduled 线程池，用于定时任务调度
  val executorService = new ScheduledThreadPoolExecutor(1)
  // 启动英雄机鼠标监听
  new HeroController(this, heroAircraft)
  private var backGroundTop = 0
  private val timeInterval = 1
  // private val enemyMaxNumber = 5
  private var gameOverFlag = false
  private var score = 0

  private var frameTime: Double = 0
  private var lastFrameTime: Double = 0

  private val heroShootDuration = 6
  private var heroShootCycleTime: Double = 0

  private val mobCreateDuration = 600
  private var mobCreateCycleTime: Double = 0

  private val eLiteCreateDuration = 600
  private var eLiteCreateCycleTime: Double = 0

  private val eLiteShootDuration = 600
  private var eLiteShootCycleTime: Double = 0

  private var fpsCycleTime: Double = 0
  private val frameCount = new ListBuffer[Double]

  def frameTimeDelta = frameTime - lastFrameTime

  /**
   * 游戏启动入口，执行游戏逻辑
   */
  def action() = {
    // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
    val task = new Runnable {
      override def run() = {
        frameTime = getTimeMills
        frameCount.append(frameTime)
        frameCount.filterInPlace(_ >= (if (frameTime >= 1000) frameTime - 1000 else 0))
        // 周期性执行（控制频率）
        // 英雄飞机射出子弹
        if (onHeroShootCountCycle) heroShootAction()
        // 敌机射出子弹
        if (onELiteShootCountCycle) eLiteShootAction()
        // 新敌机产生
        if (onMobCreateCountCycle) enemyAircrafts.append(MobEnemy.create())
        // 产生精英敌机
        if (onELiteCreateCountCycle) enemyAircrafts.append(ELiteEnemy.create())
        if (onFpsCountCycle) {
          if (frameTime < 1000) println(f"[ ${frameTime.toFloat / 1000}%.3fs ]")
          else println(f"[ ${frameTime.toFloat / 1000}%.3fs ] ${frameCount.size} fps")
        }
        // 子弹移动
        bulletsMoveAction()
        // 飞机移动
        aircraftsMoveAction()
        // 道具移动
        props.foreach(_.forward())
        // 撞击检测
        crashCheckAction()
        // 后处理
        postProcessAction()
        //每个时刻重绘界面
        repaint()
        // 游戏结束检查
        if (heroAircraft.getHp <= 0) { // 游戏结束
          executorService.shutdown()
          gameOverFlag = true
          println("Game Over!")
        }
        lastFrameTime = frameTime
      }
    }

    /**
     * 以固定延迟时间进行执行
     * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
     */
    executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS)
  }

  private def onHeroShootCountCycle = {
    heroShootCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (heroShootCycleTime >= heroShootDuration) {
      heroShootCycleTime %= heroShootDuration
      true
    } else false
  }

  private def onELiteShootCountCycle = {
    eLiteShootCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (eLiteShootCycleTime >= eLiteShootDuration) {
      eLiteShootCycleTime %= eLiteShootDuration
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

  private def onELiteCreateCountCycle = {
    eLiteCreateCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (eLiteCreateCycleTime >= eLiteCreateDuration) {
      eLiteCreateCycleTime %= eLiteCreateDuration
      true
    } else false
  }

  private def onFpsCountCycle = {
    fpsCycleTime += frameTimeDelta
    // 跨越到新的周期
    if (fpsCycleTime >= 1000) {
      fpsCycleTime %= 1000
      true
    } else false
  }

  private def heroShootAction() = {
    // 英雄射击
    heroBullets.synchronized(heroBullets.addAll(heroAircraft.shoot()))
  }

  private def eLiteShootAction() = {
    // 精英敌机射击
    enemyAircrafts.foreach(enemy => enemyBullets.synchronized(enemyBullets.addAll(enemy.shoot())))
  }

  private def bulletsMoveAction() = {
    heroBullets.foreach(_.forward())
    enemyBullets.foreach(_.forward())
  }

  private def aircraftsMoveAction() = {
    enemyAircrafts.foreach(_.forward())
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
                if (enemyAircraft.getClass.getName.endsWith("ELiteEnemy")) {
                  // 产生道具补给
                  props.append(BloodProp.create(enemyAircraft.getPos))
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
    enemyBullets.synchronized(enemyBullets.filterInPlace(_.isValid))
    heroBullets.synchronized(heroBullets.filterInPlace(_.isValid))
    enemyAircrafts.synchronized(enemyAircrafts.filterInPlace(_.isValid))
    props.synchronized(props.filterInPlace(_.isValid))
  }

  /**
   * 重写paint方法
   * 通过重复调用paint方法，实现游戏动画
   */
  override def paint(g: Graphics) = {
    super.paint(g)
    // 绘制背景,图片滚动
    g.drawImage(Background.getImage, 0, backGroundTop - Main.WINDOW_HEIGHT, null)
    g.drawImage(Background.getImage, 0, backGroundTop, null)
    backGroundTop += 1
    if (backGroundTop == Main.WINDOW_HEIGHT) backGroundTop = 0
    // 先绘制子弹，后绘制飞机
    // 这样子弹显示在飞机的下层
    paintImageWithPositionRevised(g, enemyBullets)
    paintImageWithPositionRevised(g, heroBullets)
    paintImageWithPositionRevised(g, enemyAircrafts)
    paintImageWithPositionRevised(g, props)
    g.drawImage(
      HeroAircraft.getImage,
      (heroAircraft.getLocationX - HeroAircraft.getImage.getWidth / 2).toInt,
      (heroAircraft.getLocationY - HeroAircraft.getImage.getHeight / 2).toInt,
      null
    )
    //绘制得分和生命值
    paintScoreAndLife(g)
  }

  private def paintImageWithPositionRevised(g: Graphics, objects: ListBuffer[_ <: FlyingObject]): Unit = {
    objects.synchronized {
      objects.foreach(obj => {
        val image = obj.getImage
        // println(f"paint ${(obj.getLocationX - image.getWidth / 2).toInt}, ${(obj.getLocationY - image.getHeight / 2).toInt} ${obj.getClass.getName}")
        g.drawImage(image, (obj.getLocationX - image.getWidth / 2).toInt, (obj.getLocationY - image.getHeight / 2).toInt, null)
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