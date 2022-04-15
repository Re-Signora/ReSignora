package edu.hitsz.vector;

import java.util.List;

/**
 * 一维向量
 * @author Chiro
 */
public class Scale extends VectorType {
    Scale(double x) {
        super(1, List.of(x));
    }

    public double getX() {
        return get().get(0);
    }
}
