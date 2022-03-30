package edu.hitsz.bullet;

/**
 * 子弹工厂接口
 * @author Chiro
 */
public interface BaseBulletFactory {
    /**
     * 创建新的子弹
     * @return 弹对象
     */
    BaseBullet create();
}
