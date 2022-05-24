package work.chiro.game.resource;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import work.chiro.game.objects.aircraft.AircraftHeroBox;
import work.chiro.game.objects.aircraft.BossEnemy;
import work.chiro.game.objects.aircraft.EliteEnemy;
import work.chiro.game.objects.aircraft.HeroAircraft;
import work.chiro.game.objects.aircraft.MobEnemy;
import work.chiro.game.objects.bullet.EnemyBullet;
import work.chiro.game.objects.bullet.HeroBullet;
import work.chiro.game.x.compatible.XImage;
import work.chiro.game.objects.prop.BloodProp;
import work.chiro.game.objects.prop.BombProp;
import work.chiro.game.objects.prop.BulletProp;
import work.chiro.game.utils.Utils;

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
    private final Map<String, XImage<?>> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public XImage<?> HERO_IMAGE;
    public XImage<?> HERO_BULLET_IMAGE;
    public XImage<?> ENEMY_BULLET_IMAGE;
    public XImage<?> MOB_ENEMY_IMAGE;
    public XImage<?> ELITE_ENEMY_IMAGE;
    public XImage<?> BOSS_ENEMY_IMAGE;
    public XImage<?> BLOOD_PROP_IMAGE;
    public XImage<?> BOMB_PROP_IMAGE;
    public XImage<?> BULLET_PROP_IMAGE;
    public XImage<?> BOX_HERO;

    private static ImageManager instance = null;

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                instance = new ImageManager();
            }
        }
        return instance;
    }

    ImageManager() {
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

    public XImage<?> get(String className) {
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public XImage<?> get(Class<?> clazz) {
        return get(clazz.getName());
    }

}
