package edu.hitsz.vector;

import java.util.Arrays;

/**
 * 二维向量
 * @author Chiro
 */
public class Vec2 extends VectorType {
    Vec2(double x, double y) {
        super(2, Arrays.asList(x, y));
    }

    public double getX() {
        return get().get(0);
    }

    public double getY() {
        return get().get(1);
    }
}
