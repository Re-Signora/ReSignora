package edu.hitsz.vector;

import java.util.List;

/**
 * 一维向量
 *
 * @author Chiro
 */
public class Scale extends Vec {
    public Scale() {
        this(0);
    }

    public Scale(double x) {
        super(1, List.of(x));
    }

    public double getX() {
        return get().get(0);
    }

    @Override
    public Scale copy() {
        return new Scale(getX());
    }

    @Override
    public Scale fromVector(VectorType that) {
        assert that.getSize() == 1;
        return new Scale(that.get().get(0));
    }

    @Override
    public Scale getNewInstance() {
        return new Scale();
    }
}
