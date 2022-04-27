package work.chiro.game.vector;

import java.util.LinkedList;
import java.util.List;

/**
 * 向量基类
 *
 * @author Chiro
 */
public class Vec extends VectorType implements VectorFactory<Vec> {
    public Vec(int size, List<Double> dataInit) {
        super(size, dataInit);
    }

    public Vec() {
        this(0, new LinkedList<>());
    }

    @Override
    public Vec copy() {
        return new Vec(getSize(), get());
    }

    @Override
    public Vec fromVector(VectorType that) {
        return new Vec(that.getSize(), that.get());
    }

    @Override
    public Vec getNewInstance() {
        return new Vec();
    }

    public Vec plus(Vec that) {
        return fromVector(super.plus(that));
    }

    public Vec minus(Vec that) {
        return fromVector(super.minus(that));
    }

    public Vec times(Vec that) {
        return fromVector(super.times(that));
    }

    public Vec divide(Vec that) {
        return fromVector(super.divide(that));
    }

    @Override
    public String toString() {
        return get().toString();
    }

    public Scale getScale() {
        final Scale sum = new Scale();
        get().forEach(item -> sum.plus(item * item));
        return sum;
    }
}
