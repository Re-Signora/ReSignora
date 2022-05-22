package work.chiro.game.prop;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.animate.AnimateContainerFactory;
import work.chiro.game.vector.Vec2;

/**
 * 道具工厂接口
 *
 * @author Chiro
 */
public abstract class AbstractPropFactory {
    protected Vec2 position;

    public AbstractPropFactory(Vec2 posInit) {
        this.position = posInit;
    }

    /**
     * 创建新的道具
     *
     * @return 创建的道具对象
     */
    abstract AbstractProp create();

    protected Vec2 getPosition() {
        return position;
    }

    protected AnimateContainer getAnimateContainer() {
        return new AnimateContainerFactory(
                AnimateContainerFactory.ContainerType.ConstSpeed,
                getPosition()).setupSpeed(new Vec2(0, 0.35)).create();
    }
}
