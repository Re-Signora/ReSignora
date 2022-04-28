package work.chiro.game.application;


import work.chiro.game.aircraft.*;
import work.chiro.game.bullet.EnemyBullet;
import work.chiro.game.bullet.HeroBullet;
import work.chiro.game.prop.BloodProp;
import work.chiro.game.prop.BombProp;
import work.chiro.game.prop.BulletProp;
import work.chiro.game.utils.Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, BufferedImage> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static BufferedImage HERO_IMAGE;
    public static BufferedImage HERO_BULLET_IMAGE;
    public static BufferedImage ENEMY_BULLET_IMAGE;
    public static BufferedImage MOB_ENEMY_IMAGE;
    public static BufferedImage ELITE_ENEMY_IMAGE;
    public static BufferedImage BOSS_ENEMY_IMAGE;
    public static BufferedImage BLOOD_PROP_IMAGE;
    public static BufferedImage BOMB_PROP_IMAGE;
    public static BufferedImage BULLET_PROP_IMAGE;
    public static BufferedImage BOX_HERO;

    static {
        try {
            HERO_IMAGE = Utils.getCachedImage("hero.png");
            MOB_ENEMY_IMAGE = Utils.getCachedImage("mob.png");
            ELITE_ENEMY_IMAGE = Utils.getCachedImage("elite.png");
            BOSS_ENEMY_IMAGE = Utils.getCachedImage("boss.png");
            HERO_BULLET_IMAGE = Utils.getCachedImage("bullet_hero.png");
            ENEMY_BULLET_IMAGE = Utils.getCachedImage("bullet_enemy.png");
            BLOOD_PROP_IMAGE = Utils.getCachedImage("prop_blood.png");
            BOMB_PROP_IMAGE = Utils.getCachedImage("prop_bomb.png");
            BULLET_PROP_IMAGE = Utils.getCachedImage("prop_bullet.png");
            BOX_HERO = Utils.getCachedImage("box_hero.png");

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), BLOOD_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), BOMB_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BulletProp.class.getName(), BULLET_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(AircraftHeroBox.class.getName(), BOX_HERO);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static BufferedImage get(String className) {
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static BufferedImage get(Object obj) {
        if (obj == null) {
            return null;
        }
        return get(obj.getClass().getName());
    }

}
