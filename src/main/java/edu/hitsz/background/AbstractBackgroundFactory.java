package edu.hitsz.background;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public interface AbstractBackgroundFactory {
    /**
     * 初始化位置
     * @return 初始化的位置
     */
    Vec2 initPosition();

    /**
     * 生成动画容器
     * @return 动画容器
     */
    AnimateContainer createAnimateContainer();

    /**
     * 构建背景
     * @return 背景
     */
    AbstractBackground create();
}
