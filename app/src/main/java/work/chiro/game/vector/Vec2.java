package work.chiro.game.vector;

import java.util.Arrays;

/**
 * 二维向量
 *
 * @author Chiro
 */
public class Vec2 extends Vec {
    public Vec2() {
        this(0, 0);
    }

    public Vec2(double x, double y) {
        super(2, Arrays.asList(x, y));
    }

    public double getX() {
        return get().get(0);
    }

    public double getY() {
        return get().get(1);
    }

    public Vec2 setX(double x) {
        get().set(0, x);
        return this;
    }

    public Vec2 setY(double y) {
        get().set(1, y);
        return this;
    }

    public void set(double x, double y) {
        set(Arrays.asList(x, y));
    }

    @Override
    public Vec2 copy() {
        return new Vec2(getX(), getY());
    }

    @Override
    public Vec2 fromVector(VectorType that) {
        assert that.getSize() == 2;
        return new Vec2(that.get().get(0), that.get().get(1));
    }

    @Override
    public Vec2 getNewInstance() {
        return new Vec2();
    }

    public Vec2 plus(Vec2 that) {
        return fromVector(super.plus(that));
    }

    public Vec2 minus(Vec2 that) {
        return fromVector(super.minus(that));
    }

    public Vec2 times(Vec2 that) {
        return fromVector(super.times(that));
    }

    public Vec2 divide(Vec2 that) {
        return fromVector(super.divide(that));
    }
}
