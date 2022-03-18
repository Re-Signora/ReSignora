package edu.hitsz.application

import edu.hitsz.aircraft.{HeroAircraft, MobEnemy}
import edu.hitsz.bullet.{EnemyBullet, HeroBullet}

import java.awt.image.BufferedImage
import java.io.{FileInputStream, FileNotFoundException, IOException}
import java.util
import javax.imageio.ImageIO

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author chiro2001
 */
object ImageManager {
  /**
   * 类名-图片 映射，存储各基类的图片 <br>
   * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
   */
  private val CLASSNAME_IMAGE_MAP = new util.HashMap[String, BufferedImage]

  def tryGetImageFile(filename: String): BufferedImage = {
    def getImage(path: String) = ImageIO.read(new FileInputStream(getClass.getResource(path).getFile))

    try {
      getImage(f"/$filename")
    } catch {
      case e: FileNotFoundException => getImage(f"$filename")
    }
  }

  val BACKGROUND_IMAGE = tryGetImageFile("images/bg.jpg")
  val HERO_IMAGE = tryGetImageFile("images/hero.png")
  val MOB_ENEMY_IMAGE = tryGetImageFile("images/mob.png")
  val HERO_BULLET_IMAGE = tryGetImageFile("images/bullet_hero.png")
  val ENEMY_BULLET_IMAGE = tryGetImageFile("images/bullet_enemy.png")
  CLASSNAME_IMAGE_MAP.put(classOf[HeroAircraft].getName, HERO_IMAGE)
  CLASSNAME_IMAGE_MAP.put(classOf[MobEnemy].getName, MOB_ENEMY_IMAGE)
  CLASSNAME_IMAGE_MAP.put(classOf[HeroBullet].getName, HERO_BULLET_IMAGE)
  CLASSNAME_IMAGE_MAP.put(classOf[EnemyBullet].getName, ENEMY_BULLET_IMAGE)

  // val test = classOf[this]

  // def get[T](c: Class[T]) = c match {
  //   case () => 1
  // }

  def get(className: String) = CLASSNAME_IMAGE_MAP.get(className)

  def get(obj: Any): BufferedImage = {
    if (obj == null) return null
    get(obj.getClass.getName)
  }
}
