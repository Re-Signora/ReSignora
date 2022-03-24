package edu.hitsz.application

import edu.hitsz.aircraft._
import edu.hitsz.animate.{AnimateContainer, AnimateLinear, AnimateVectorType}
import edu.hitsz.basic.FlyingObject
import edu.hitsz.basic.PositionType.Position
import edu.hitsz.bullet.AbstractBullet
import edu.hitsz.scene.Background
import edu.hitsz.utils.getTimeMills

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
  val heroPosition = new Position(Main.WINDOW_WIDTH / 2, Main.WINDOW_HEIGHT - Background.getImage.getHeight)
  val heroAircraft = new HeroAircraft(heroPosition, new AnimateContainer[Position], 100)
  val enemyAircrafts = new ListBuffer[AbstractAircraft]
  val heroBullets = new ListBuffer[AbstractBullet]
  val enemyBullets = new ListBuffer[AbstractBullet]
  //Scheduled 线程池，用于定时任务调度
  val executorService = new ScheduledThreadPoolExecutor(1)
  //启动英雄机鼠标监听
  new HeroController(this, heroAircraft)
  private var backGroundTop = 0
  // 时间间隔(ms)，控制刷新频率
  private val timeInterval = 10
  private val timeStart = System.currentTimeMillis
  private val enemyMaxNumber = 5
  private var gameOverFlag = false
  private var score = 0
  private var time: Long = 0
  /**
   * 周期（ms)
   * 指示子弹的发射、敌机的产生频率
   */
  private val bulletDuration = 600
  private var cycleTime = 0
  private val frameCount = new ListBuffer[Long]

  /**
   * 游戏启动入口，执行游戏逻辑
   */
  def action() = {
    // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
    val task = new Runnable {
      override def run() = {
        // time += timeInterval
        time = System.currentTimeMillis - timeStart
        frameCount.append(time)
        frameCount.filterInPlace(_ >= (if (time >= 1000) time - 1000 else 0))
        // 周期性执行（控制频率）
        if (timeCountAndNewCycleJudge) {
          if (time < 1000) println(f"[ ${time.toFloat / 1000}%.3fs ]")
          else println(f"[ ${time.toFloat / 1000}%.3fs ] ${frameCount.size} fps")
          // 新敌机产生
          if (enemyAircrafts.size < enemyMaxNumber) enemyAircrafts.append({
            val positionEnemyNew = new Position(
              (Math.random * (Main.WINDOW_WIDTH - MobEnemy.getImage.getWidth)).toInt * 1,
              (Math.random * Main.WINDOW_HEIGHT * 0.2).toInt * 1
            )
            new MobEnemy(
              positionEnemyNew, new AnimateContainer[Position](List(
                new AnimateLinear(positionEnemyNew, new Position(
                  (Math.random * (Main.WINDOW_WIDTH - MobEnemy.getImage.getWidth)).toInt * 1, 0), AnimateVectorType.PositionLike.id, getTimeMills, 2000)
              )), 30
            )
          }
          )
          // 飞机射出子弹
          shootAction()
        }
        // 子弹移动
        bulletsMoveAction()
        // 飞机移动
        aircraftsMoveAction()
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
      }
    }

    /**
     * 以固定延迟时间进行执行
     * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
     */
    executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS)
  }

  private def timeCountAndNewCycleJudge = {
    cycleTime += timeInterval
    // 跨越到新的周期
    if (cycleTime >= bulletDuration && cycleTime - timeInterval < cycleTime) {
      cycleTime %= bulletDuration
      true
    }
    else false
  }

  private def shootAction() = { // TODO 敌机射击
    // 英雄射击
    heroBullets.addAll(heroAircraft.shoot())
  }

  private def bulletsMoveAction() = {
    for (bullet <- heroBullets) {
      bullet.forward()
    }
    for (bullet <- enemyBullets) {
      bullet.forward()
    }
  }

  private def aircraftsMoveAction() = {
    for (enemyAircraft <- enemyAircrafts) {
      enemyAircraft.forward()
    }
  }

  /**
   * 碰撞检测：
   * 1. 敌机攻击英雄
   * 2. 英雄攻击/撞击敌机
   * 3. 英雄获得补给
   */
  private def crashCheckAction() = {
    // TODO 敌机子弹攻击英雄
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
                // TODO 获得分数，产生道具补给
                score += 10
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
    // Todo: 我方获得道具，道具生效
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