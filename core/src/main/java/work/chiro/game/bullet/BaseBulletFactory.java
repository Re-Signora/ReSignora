package work.chiro.game.bullet;

import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

import java.util.Collections;
import java.util.List;

/**
 * 子弹工厂接口
 *
 * @author Chiro
 */
public abstract class BaseBulletFactory {
    public Vec2 getPosition() {
        return position;
    }

    protected final Vec2 position;

    BaseBulletFactory(AbstractConfig config, Vec2 posInit) {
        this.config = config;
        this.position = posInit;
    }

    protected final AbstractConfig config;

    /**
     * 创建新的子弹
     *
     * @return 弹对象
     */
    abstract BaseBullet create();

    public List<BaseBullet> createMany(int requireNum) {
        return Collections.nCopies(requireNum, create());
    }
}
