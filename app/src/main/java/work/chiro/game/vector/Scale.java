package work.chiro.game.vector;

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

    public void setX(double x) {
        get().set(0, x);
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

    public Scale plus(Scale that) {
        return fromVector(super.plus(that));
    }

    public Scale minus(Scale that) {
        return fromVector(super.minus(that));
    }

    public Scale times(Scale that) {
        return fromVector(super.times(that));
    }

    public Scale divide(Scale that) {
        return fromVector(super.divide(that));
    }

    @Override
    public String toString() {
        return String.format("%.0f", getX());
    }
}
