package work.chiro.game.background;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.application.Main;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public abstract class AbstractBackgroundFactory {
    /**
     * 构建背景
     *
     * @return 背景
     */
    abstract AbstractBackground create();

    Vec2 position = new Vec2(0, 0);

    /**
     * 初始化位置
     *
     * @return 初始化的位置
     */
    public Vec2 initPosition() {
        return position;
    }

    /**
     * 生成动画容器
     *
     * @return 动画容器
     */
    public AnimateContainer createAnimateContainer() {
        return new AnimateContainerFactory(AnimateContainerFactory.ContainerType.ConstSpeedLoop, position)
                .setupSpeed(new Vec2(0, 0.5))
                .setupRange(new Vec2(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT))
                .create();
    }
}
