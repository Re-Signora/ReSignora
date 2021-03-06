package work.chiro.game.objects.bullet;

import java.util.Collections;
import java.util.List;

import work.chiro.game.vector.Vec2;

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

    BaseBulletFactory(Vec2 posInit) {
        this.position = posInit;
    }

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
